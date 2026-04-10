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

const btnLogin = document.getElementById("openLoginModal");
const btnLogout = document.getElementById("logoutBtn");
if(btnLogout) btnLogout.style.display = 'inline-block';
if(btnLogin) btnLogin.style.display = 'none';
