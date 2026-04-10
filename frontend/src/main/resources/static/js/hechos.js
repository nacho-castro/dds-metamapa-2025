// =========================
// FUNCIÓN GENÉRICA PARA MODALES
// =========================
function initModal(modalId, openBtnId, rolesPermitidos = []) {
  const modal = document.getElementById(modalId);
  // Permitimos que openBtnId sea null/undefined comprobando si existe antes de buscarlo
  const openBtn = openBtnId ? document.getElementById(openBtnId) : null;

  // Debug: Ver en la consola si encuentra los elementos
  if (!modal) console.warn(`Warning: No se encontró el modal con ID: ${modalId}`);
  // Solo avisar si se esperaba un botón y no se encontró
  if (openBtnId && !openBtn) console.warn(`Warning: No se encontró el botón con ID: ${openBtnId}`);

  // FIX: Solo retornamos si falta el MODAL. Si falta el botón, seguimos para activar el cierre (X, Escape, etc)
  if (!modal) return;

  const closeBtn = modal.querySelector(".close") || modal.querySelector(".close-btn");

  // 1. ABRIR MODAL (Solo si hay botón único definido)
  if (openBtn) {
    openBtn.addEventListener("click", (e) => {
      e.preventDefault(); // Evita saltos de página o recargas

      // Verificación de ROL (si aplica)
      const rol = localStorage.getItem("rol");
      if (rolesPermitidos.length > 0 && !rolesPermitidos.includes(rol)) {
        alert("No tenés permisos para acceder a esta función.");
        return;
      }

      modal.style.display = "block";
    });
  }

  // 2. CERRAR CON LA 'X'
  if (closeBtn) {
    closeBtn.addEventListener("click", () => {
      modal.style.display = "none";
    });
  }

  // 3. CERRAR CLIC FUERA (Click en el fondo oscuro)
  window.addEventListener("click", (e) => {
    if (e.target === modal) {
      modal.style.display = "none";
    }
  });

  // 4. CERRAR CON ESCAPE
  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape" && modal.style.display === "block") {
      modal.style.display = "none";
    }
  });
}

// =========================
//  SOLICITUD DE ELIMINACIÓN
// =========================
document.addEventListener("DOMContentLoaded", () => {
  // Inicializamos el modal (sin botón de apertura, porque son varios dinámicos)
  // Esto activa la X y el cierre con Escape
  initModal("solicitudEliminacionModal", null);

  const btnsEliminacion = document.querySelectorAll('.btn-solicitud-eliminacion');
  const modalEliminacion = document.getElementById('solicitudEliminacionModal');

  if(btnsEliminacion.length > 0 && modalEliminacion) {
    btnsEliminacion.forEach(btn => {
      btn.addEventListener('click', function() {
        const hechoId = this.getAttribute('data-hecho-id');
        const inputId = document.getElementById('hechoIdInput'); // Asegurate que este ID exista en tu fragment
        if(inputId) inputId.value = hechoId;
        modalEliminacion.style.display = 'block';
      });
    });
  }
});

function cerrarSolicitudEliminacion() {
  const m = document.getElementById('solicitudEliminacionModal');
  if(m) m.style.display = 'none';
}


// =========================
// SWITCH MODO EDICIÓN
// =========================
document.addEventListener("DOMContentLoaded", function () {
  const editSwitch = document.getElementById("editingswitch");
  const modoTexto = document.getElementById("modoTexto");

  if (editSwitch && modoTexto) {
    function actualizarTexto() {
      modoTexto.textContent = editSwitch.checked
          ? "Modo de navegación: Irrestricta"
          : "Modo de navegación: Curada";
    }

    // Cargar estado guardado
    if (localStorage.getItem("editMode") === "true") {
      editSwitch.checked = true;
      document.body.classList.add("edit-mode");
    }
    actualizarTexto(); // Inicializar texto correcto

    // Detectar cambios
    editSwitch.addEventListener("change", function () {
      document.body.classList.toggle("edit-mode", editSwitch.checked);
      localStorage.setItem("editMode", editSwitch.checked ? "true" : "false");
      actualizarTexto();
    });
  }
});

// =========================
// SOLICITUD DE EDICIÓN
// =========================
document.addEventListener("DOMContentLoaded", () => {

  // 1. Inicializar Modal para que funcione cerrar con X y Escape
  initModal("solicitudEdicionModal", null);

  // 2. Lógica para botones "Sol. Edición"
  const btnsEdicion = document.querySelectorAll('.btn-solicitud-edicion');
  const modalEdicion = document.getElementById('solicitudEdicionModal');

  if (btnsEdicion.length > 0 && modalEdicion) {
    btnsEdicion.forEach(btn => {
      btn.addEventListener('click', function(e) {
        e.preventDefault();

        // Obtener datos del botón
        const id = this.getAttribute('data-id');
        const titulo = this.getAttribute('data-titulo');
        const desc = this.getAttribute('data-descripcion');
        const cat = this.getAttribute('data-categoria');

        // Llenar el formulario del modal
        // Asegúrate de que estos IDs existen en tu fragment de edicion-modal
        if(document.getElementById('editHechoId')) document.getElementById('editHechoId').value = id;
        if(document.getElementById('editTitulo')) document.getElementById('editTitulo').value = titulo;
        if(document.getElementById('editDescripcion')) document.getElementById('editDescripcion').value = desc;
        if(document.getElementById('editCategoria')) document.getElementById('editCategoria').value = cat;

        // Abrir modal
        modalEdicion.style.display = 'block';
      });
    });
  }

  // Cerrar modal de edición con la X (redundancia por seguridad, aunque initModal ya lo hace)
  const closeEdicionBtn = document.getElementById("closeEdicionModal");
  if(closeEdicionBtn && modalEdicion) {
    closeEdicionBtn.onclick = () => modalEdicion.style.display = "none";
  }
});

// =========================
// INICIALIZACIÓN DE OTROS MODALES GLOBALES
// =========================
document.addEventListener("DOMContentLoaded", () => {
  // Inicializamos los modales con sus IDs exactos
  initModal("loginModal", "openLoginModal");
  initModal("detalleHechoModal", "openDetalleHechoModal");
  initModal("filtroModal", "openFiltroModal");

  // Otros modales si existen en tus fragments
  initModal("coleccionModal", "openColeccionModal");
  initModal("hechoModal", "openHechoModal");
  initModal("fuenteModal", "openFuenteModal");
  initModal("criterioModal", "openCriterioModal");
});