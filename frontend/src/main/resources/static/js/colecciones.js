// =========================
// MODAL GENÉRICO
// =========================
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
  modal.addEventListener("click", (e) => {
    if (!modalContent.contains(e.target)) {
      modal.style.display = "none";
      document.body.style.overflow = "";
    }
      if (e.target === modal) {
          modal.style.display = "none";
          document.body.style.overflow = "";
      }
  });

  // Cerrar con Escape
  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape" && modal.style.display === "block") {
      modal.style.display = "none";
      document.body.style.overflow = "";
    }
  });
}

function agregarCampoUrl() {
    const container = document.getElementById('lista-multimedia');
    const divRow = document.createElement('div');
    divRow.className = 'input-row';
    divRow.style.display = 'flex';
    divRow.style.gap = '10px';
    divRow.style.marginBottom = '5px';

    const input = document.createElement('input');
    input.type = 'text';
    input.name = 'multimediaURLs'; // tiene que coincidir con los DTOs
    input.placeholder = 'https://ingrese-otra-url.com';
    input.required = false;
    input.style.flex = '1';

    const btnDelete = document.createElement('button');
    btnDelete.type = 'button';
    btnDelete.innerText = 'X';
    btnDelete.style.backgroundColor = '#ff4d4d'; // Color rojo
    btnDelete.style.color = 'white';
    btnDelete.style.border = 'round';
    btnDelete.style.cursor = 'pointer';

    btnDelete.onclick = function() {
        container.removeChild(divRow);
    };

    divRow.appendChild(input);
    divRow.appendChild(btnDelete);
    container.appendChild(divRow);
}


// =========================
// AGREGAR FUENTES DINÁMICAMENTE
// =========================
document.addEventListener('DOMContentLoaded', () => {
  const addFuenteBtn = document.getElementById('addFuenteBtn');
  const container = document.getElementById('fuentesContainer');
  const addCriterioBtn = document.getElementById('addCriterioBtn');
  const containerCriterio = document.getElementById('criteriosContainer');

  if (addFuenteBtn && container) {
      addFuenteBtn.addEventListener('click', () => {
        // Verificar que window.fuentes exista
        const fuentesDisponibles = window.fuentes || [];

        console.log('=== CLICK EN AGREGAR FUENTE ===');
        console.log('window.fuentes existe:', typeof window.fuentes !== 'undefined');
        console.log('Cantidad de fuentes:', fuentesDisponibles.length);
        console.log('Fuentes disponibles:', fuentesDisponibles);

        if (fuentesDisponibles.length === 0) {
          alert('No hay fuentes disponibles. Por favor, cree una fuente primero.');
          return;
        }

        const div = document.createElement('div');
        div.classList.add("fuente-item");

        div.innerHTML = `
          <div class="fuente-fields">
              <div class="field-group">
                <label>Seleccionar Fuente Existente</label>
                <select name="fuentes" required>
                  <option value="">-- Seleccione --</option>
                   ${fuentes.map(f => `<option value="${f.id}">${f.tipoFuente} ⮕ ${f.path}"</option>`).join("")}
                </select>
              </div>

              <button type="button" class="removeFuenteBtn">Remover</button>
          </div>
          <hr>
      `;

      container.appendChild(div);

      // Eliminar fuente
      div.querySelector('.removeFuenteBtn').addEventListener('click', () => {
        div.remove();
      });
    });
  }
  if (addCriterioBtn && containerCriterio) {
        addCriterioBtn.addEventListener('click', () => {
            // Verificar que window.criterios exista
            const criteriosDisponibles = window.criterios || [];

            console.log('=== CLICK EN AGREGAR CRITERIO ===');
            console.log('window.criterios existe:', typeof window.criterios !== 'undefined');
            console.log('Cantidad de criterios:', criteriosDisponibles.length);
            console.log('Criterios disponibles:', criteriosDisponibles);

            if (criteriosDisponibles.length === 0) {
                alert('No hay criterios disponibles. Por favor, cree una criterio primero.');
                return;
            }

            const div = document.createElement('div');
            div.classList.add("criterio-item");

            div.innerHTML = `
          <div class="criterio-fields">
              <div class="field-group">
                <label>Seleccionar Criterio Existente</label>
                <select name="criterios" required>
                  <option value="">-- Seleccione --</option>
                   ${criterios.map(c => `<option value="${c.id}">ID: ${c.id}</option>`).join("")}
                </select>
              </div>

              <button type="button" class="removeFuenteBtn">Remover</button>
          </div>
          <hr>
      `;

            containerCriterio.appendChild(div);

            // Eliminar criterio
            div.querySelector('.removeCriterioBtn').addEventListener('click', () => {
                div.remove();
            });
        });
  }

  // =========================
  // INICIO DE MODALES
  // =========================
  // Solo inicializar modales que existan en la página
  if (document.getElementById('coleccionModal') && document.getElementById('openColeccionModal')) {
    initModal("coleccionModal", "openColeccionModal");
  }

  if (document.getElementById('hechoModal') && document.getElementById('openHechoModal')) {
    initModal("hechoModal", "openHechoModal");
  }
  if (document.getElementById('fuenteModal') && document.getElementById('openFuenteModal')) {
    initModal("fuenteModal", "openFuenteModal");
  }

  if (document.getElementById('criterioModal') && document.getElementById('openCriterioModal')) {
    initModal("criterioModal", "openCriterioModal");
  }
  // NO inicializamos editarColeccionModal aquí porque se maneja con onclick inline
});

document.addEventListener("DOMContentLoaded", () => {
    const editButtons = document.querySelectorAll(".edit-btn");

    editButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            const card = btn.closest(".card");

            const id = card.querySelector("button").getAttribute("onclick").match(/\d+/)[0];
            const titulo = card.querySelector("h3").innerText;
            const descripcion = card.querySelector("p").innerText;

            // Cargar valores
            document.getElementById("titulo").value = titulo;
            document.getElementById("descripcion").value = descripcion;

            // Setear acción del formulario
            document.getElementById("editarColeccionForm").action = `/colecciones/${id}/editar`;

            // Abrir modal
            document.getElementById("editarColeccionModal").style.display = "block";
        });
    });
});

function cerrarModal(id) {
    document.getElementById(id).style.display = "none";
}

// =========================
// FUNCIONES GLOBALES
// =========================

// Función para ocultar mensajes automáticamente
document.addEventListener('DOMContentLoaded', () => {
  const alerts = document.querySelectorAll('.alert');
  alerts.forEach(alert => {
    setTimeout(() => {
      alert.style.opacity = '0';
      alert.style.transition = 'opacity 0.5s ease';
      setTimeout(() => alert.remove(), 500);
    }, 5000); // Desaparece después de 5 segundos
  });
});

function abrirModalEditarFuente(btn) {
    const id = btn.dataset.id;

    document.getElementById("editarNombre").value = btn.dataset.nombre;
    document.getElementById("editarTipoFuente").value = btn.dataset.tipo;
    document.getElementById("editarPath").value = btn.dataset.path;
    document.getElementById("editarPathInfo").value = btn.dataset.pathinfo;

    const form = document.getElementById("editarFuenteForm");
    form.action = `/fuentes/${id}/editar`;

    document.getElementById("editarFuenteModal").style.display = "block";
}

