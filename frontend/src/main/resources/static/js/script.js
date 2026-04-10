
// ----------------------
// Inicialización de modales
// ----------------------

//La lógica para abrir/cerrar se abstrae.
//Si mañana se agrega otro modal, solo hay que llamarlo

function initModal(modalId, openBtnId) {
  const modal = document.getElementById(modalId);
  const openBtn = document.getElementById(openBtnId);
  if (!modal || !openBtn) return; // si no existe, no hace nada

  const closeBtn = modal.querySelector(".close");
  const modalContent = modal.querySelector(".modal-content");

  // Abrir
  openBtn.addEventListener("click", () => {
    modal.style.display = "block";
    document.body.style.overflow = "hidden"; // evita scroll de fondo
  });

  // Cerrar con la X
  if (closeBtn) {
    closeBtn.addEventListener("click", () => {
      modal.style.display = "none";
      document.body.style.overflow = "";
    });
  }

  // Cerrar clic fuera del contenido
  modal.addEventListener("mousedown", (e) => {
    if (!modalContent.contains(e.target)) {
      modal.style.display = "none";
      document.body.style.overflow = "";
    }
  });

  // Cerrar con Escape
  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") {
      modal.style.display = "none";
      document.body.style.overflow = "";
    }
  });
}

// ----------------------
// Login
// ----------------------

//Login vía fetch
//Llama a /auth con POST JSON.
//Spring Security y CustomAuthProvider manejan autenticación y sesión.
//Luego recarga la página
document.getElementById('loginForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  console.log("Login submit triggered");

  const submitBtn = e.target.querySelector('button[type="submit"]');
  const originalText = submitBtn.textContent;
  submitBtn.disabled = true;
  submitBtn.textContent = 'Iniciando sesión...';

  const username = e.target.username.value;
  const password = e.target.password.value;

  try {
    const response = await fetch('/auth', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-Requested-With': 'XMLHttpRequest'  // Mark as AJAX request
      },
      credentials: 'include',
      body: JSON.stringify({ username, password })
    });

    const data = await response.json();

    if (response.ok && data.success) {
      // Close modal
      const modal = document.getElementById('loginModal');
      modal.style.display = 'none';
      document.body.style.overflow = '';

      // Refresh UI
      await manejarSesion();

      // Show success message
      alert('¡Sesión iniciada con éxito!');

    } else {
      alert(data.message || 'Usuario o contraseña incorrectos');
      submitBtn.disabled = false;
      submitBtn.textContent = originalText;
    }
  } catch (error) {
    console.error('Error during login:', error);
    alert('Error de conexión. Por favor, intente nuevamente.');
    submitBtn.disabled = false;
    submitBtn.textContent = originalText;
  }
});

// =========================
// LOGOUT
// =========================

//Logout vía fetch
//Llama a /auth/logout.
//Invalida sesión y contexto de Spring Security.

document.getElementById("logoutBtn").addEventListener("click", async () => {
  try {
    await fetch('/auth/logout', { method: 'GET', credentials: 'same-origin' });
    await manejarSesion();
    alert("Sesión cerrada correctamente", "success");
  } catch {
    alert("Error al cerrar sesión", "error");
  }
});

// =========================
// MANEJO DE SESIÓN
// =========================

//Manejo de sesión en frontend
//Pregunta al backend (GET /auth) si hay usuario logeado y cuáles son sus roles.
//Muestra/oculta botones (login, logout, panel) según la sesión real del servidor.

async function manejarSesion() {
  const btnLogin = document.getElementById("openLoginModal");
  const btnLogout = document.getElementById("logoutBtn");
  const btnPanel = document.getElementById("panelBtn");

  // 1. OBTENER LOS BOTONES QUE FALTABAN
  const btnCargarColeccion = document.getElementById("openColeccionModal");
  const btnCargarCriterio = document.getElementById("openCriterioModal"); // Nuevo
  const btnCargarFuente = document.getElementById("openFuenteModal");     // Nuevo

  // Estado por defecto (visitante) -> OCULTAR TODO AL PRINCIPIO
  if (btnLogin) btnLogin.style.display = "inline-block";
  if (btnLogout) btnLogout.style.display = "none";
  if (btnPanel) btnPanel.style.display = "none";

  // 2. OCULTARLOS POR DEFECTO
  if (btnCargarColeccion) btnCargarColeccion.style.display = "none";
  if (btnCargarCriterio) btnCargarCriterio.style.display = "none"; // Nuevo
  if (btnCargarFuente) btnCargarFuente.style.display = "none";     // Nuevo

  try {
    const response = await fetch("/auth");
    if (!response.ok) throw new Error("No logeado");

    const user = await response.json();
    if (!user.username) return; // visitante

    const roles = user.authorities.map(a => a.authority);

    // Ocultar login, mostrar logout
    if (btnLogin) btnLogin.style.display = "none";
    if (btnLogout) btnLogout.style.display = "inline-block";

    // Si es admin mostrar panel y botones de carga
    if (roles.includes("ROLE_ADMINISTRADOR")) {
      if (btnPanel) btnPanel.style.display = "inline-block";

      // 3. MOSTRARLOS SI ES ADMIN
      if (btnCargarColeccion) btnCargarColeccion.style.display = "inline-block";
      if (btnCargarCriterio) btnCargarCriterio.style.display = "inline-block"; // Nuevo
      if (btnCargarFuente) btnCargarFuente.style.display = "inline-block";     // Nuevo
    }
  } catch (e) {
    // visitante: no hacer nada
  }
}

// =========================
// INICIO
// =========================

//DOMContentLoaded inicializa el modal y actualiza la UI según la sesión.
document.addEventListener("DOMContentLoaded", () => {
  initModal("loginModal", "openLoginModal");
  manejarSesion();
});


