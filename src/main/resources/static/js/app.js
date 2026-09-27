/**
 * app.js – Shared API helpers and UI utilities
 * Vehicle Service Management System
 */

'use strict';

/* =============================================
   API HELPERS
   ============================================= */

const BASE_URL = '';  // same origin – served by Spring Boot

/**
 * Generic fetch wrapper. Returns parsed JSON on success.
 * Throws an Error with .message on failure.
 */
async function apiFetch(method, path, body = null) {
  const opts = {
    method,
    headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' }
  };
  if (body !== null) opts.body = JSON.stringify(body);

  let res;
  try {
    res = await fetch(BASE_URL + path, opts);
  } catch (e) {
    throw new Error('Network error – cannot reach the server. Is Spring Boot running?');
  }

  if (res.status === 204) return null; // No Content

  let data;
  try { data = await res.json(); } catch { data = null; }

  if (!res.ok) {
    const msg = data?.message || `HTTP ${res.status}: ${res.statusText}`;
    throw new Error(msg);
  }
  return data;
}

const apiGet    = (path)        => apiFetch('GET',    path);
const apiPost   = (path, body)  => apiFetch('POST',   path, body);
const apiPut    = (path, body)  => apiFetch('PUT',    path, body || {});
const apiDelete = (path)        => apiFetch('DELETE', path);
const apiPatch  = (path, body)  => apiFetch('PATCH',  path, body || {});

/* =============================================
   SIDEBAR NAVIGATION
   ============================================= */

function initSidebar() {
  const toggle  = document.getElementById('menuToggle');
  const sidebar = document.getElementById('sidebar');
  const overlay = document.getElementById('sidebarOverlay');
  if (!toggle || !sidebar) return;

  toggle.addEventListener('click', () => {
    sidebar.classList.toggle('open');
    overlay.classList.toggle('show');
  });
  overlay?.addEventListener('click', () => {
    sidebar.classList.remove('open');
    overlay.classList.remove('show');
  });

  // Highlight the active nav item based on current page
  const current = location.pathname.split('/').pop() || 'index.html';
  document.querySelectorAll('.nav-item[data-page]').forEach(el => {
    if (el.dataset.page === current) el.classList.add('active');
  });
}

/* =============================================
   ALERT / NOTIFICATION COMPONENT
   ============================================= */

/**
 * Show an alert inside `container`.
 * type: 'success' | 'danger' | 'warning' | 'info'
 */
function showAlert(container, message, type = 'danger', autoClose = 5000) {
  if (!container) return;
  const icons = { success: '✅', danger: '❌', warning: '⚠️', info: 'ℹ️' };
  const el = document.createElement('div');
  el.className = `alert alert-${type}`;
  el.innerHTML = `
    <span class="alert-icon">${icons[type] || 'ℹ️'}</span>
    <span>${escHtml(message)}</span>
    <button class="alert-close" aria-label="Close">×</button>
  `;
  el.querySelector('.alert-close').addEventListener('click', () => el.remove());
  container.prepend(el);
  if (autoClose > 0) setTimeout(() => el.remove(), autoClose);
  return el;
}

function clearAlerts(container) {
  if (!container) return;
  container.querySelectorAll('.alert').forEach(a => a.remove());
}

/* =============================================
   MODAL HELPERS
   ============================================= */

function openModal(id) {
  const el = document.getElementById(id);
  if (el) { el.classList.remove('hidden'); document.body.style.overflow = 'hidden'; }
}
function closeModal(id) {
  const el = document.getElementById(id);
  if (el) { el.classList.add('hidden'); document.body.style.overflow = ''; }
}

// Close on overlay click
document.addEventListener('click', e => {
  if (e.target.matches('.modal-overlay')) {
    e.target.classList.add('hidden');
    document.body.style.overflow = '';
  }
  if (e.target.matches('[data-dismiss="modal"]')) {
    const modal = e.target.closest('.modal-overlay');
    if (modal) { modal.classList.add('hidden'); document.body.style.overflow = ''; }
  }
});

/* =============================================
   TABLE HELPERS
   ============================================= */

/** Render a loading row spanning `cols` columns */
function tableLoading(tbody, cols = 5) {
  tbody.innerHTML = `
    <tr class="loading-row">
      <td colspan="${cols}">
        <span class="spinner spinner-dark"></span>&nbsp; Loading...
      </td>
    </tr>`;
}

/** Render an empty-state row */
function tableEmpty(tbody, message = 'No records found.', cols = 5) {
  tbody.innerHTML = `
    <tr>
      <td colspan="${cols}">
        <div class="empty-state">
          <div class="empty-icon">📭</div>
          <p>${escHtml(message)}</p>
        </div>
      </td>
    </tr>`;
}

/* =============================================
   BADGE HELPERS
   ============================================= */

function bookingStatusBadge(status) {
  const map = {
    BOOKED:      'badge-blue',
    ASSIGNED:    'badge-cyan',
    IN_PROGRESS: 'badge-yellow',
    COMPLETED:   'badge-green',
    CANCELLED:   'badge-red'
  };
  return `<span class="badge ${map[status] || 'badge-gray'}">${status || '—'}</span>`;
}

function paymentStatusBadge(status) {
  return status === 'PAID'
    ? '<span class="badge badge-green">PAID</span>'
    : '<span class="badge badge-yellow">PENDING</span>';
}

function availabilityBadge(status) {
  if (status === 'AVAILABLE')   return '<span class="badge badge-green">AVAILABLE</span>';
  if (status === 'BUSY')        return '<span class="badge badge-yellow">BUSY</span>';
  if (status === 'UNAVAILABLE') return '<span class="badge badge-red">UNAVAILABLE</span>';
  return `<span class="badge badge-gray">${escHtml(status || '—')}</span>`;
}

/* =============================================
   FORM HELPERS
   ============================================= */

function setLoading(btn, loading, label = 'Save') {
  btn.disabled = loading;
  btn.innerHTML = loading
    ? `<span class="spinner"></span> Saving...`
    : label;
}

function getFormData(form) {
  const data = {};
  new FormData(form).forEach((val, key) => {
    data[key] = val.trim() === '' ? null : val;
  });
  return data;
}

/* =============================================
   UTILITY
   ============================================= */

function escHtml(str) {
  if (str == null) return '';
  return String(str)
    .replace(/&/g, '&amp;').replace(/</g, '&lt;')
    .replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

function formatDate(d) {
  if (!d) return '—';
  return new Date(d).toLocaleDateString('en-IN', { year: 'numeric', month: 'short', day: '2-digit' });
}

function formatCurrency(val) {
  if (val == null) return '—';
  return '₹' + Number(val).toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

/* ---- init on load ---- */
document.addEventListener('DOMContentLoaded', initSidebar);
