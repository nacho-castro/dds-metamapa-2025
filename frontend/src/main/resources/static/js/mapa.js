document.addEventListener("DOMContentLoaded", () => {

    if (!window.hechosData) {
        console.error("hechosData no está definido");
        return;
    }

    const map = L.map('map').setView([-38, -64], 5);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18
    }).addTo(map);

    window.hechosData.forEach(h => {
        if (h.lugarAcontecimiento) {
            const lat = h.lugarAcontecimiento.latitud;
            const lon = h.lugarAcontecimiento.longitud;

            L.marker([lat, lon])
                .addTo(map)
                .bindPopup(h.titulo);
        }
    });
});
