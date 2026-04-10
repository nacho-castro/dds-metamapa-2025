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

// =========================
// ACEPTAR SOLICITUD
// =========================
document.querySelectorAll(".aceptarSoli").forEach(btn => {
    btn.addEventListener("click", async () => {
        const solicitudId = btn.dataset.id; // obtiene el data-id del botón

        try {
            console.log("EL ID DE LA SOLICITUD ES " + solicitudId);
            const response = await fetch(`http://localhost:8081/api/solicitudes/eliminacion/${solicitudId}/aprobar`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                }
            });
            if (response.ok) {
                alert("Solicitud de eliminación aprobada ✅");
                const card = btn.closest(".card");
                if (card) card.remove();
            }
            if (!response.ok) throw new Error("Error en el servidor");

        } catch (error) {
            alert("Error al aprobar la solicitud ❌");
            console.error(error);
        }
    });
});
// =========================
// RECHAZAR SOLICITUD
// =========================
document.querySelectorAll(".rechazarSoli").forEach(btn => {
    btn.addEventListener("click", async () => {
        const solicitudId = btn.dataset.id; // obtiene el data-id del botón

        try {
            console.log("EL ID DE LA SOLICITUD PARA RECHAZAR ES " + solicitudId);
            const response = await fetch(`http://localhost:8081/api/solicitudes/eliminacion/${solicitudId}/denegar`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                }
            });
            if (response.ok) {
                alert("Solicitud de eliminación rechazada ✅");
                const card = btn.closest(".card");
                if (card) card.remove();
            }
            if (!response.ok) throw new Error("Error en el servidor");

        } catch (error) {
            alert("Error al rechazar la solicitud ❌");
            console.error(error);
        }
    });
});
