//-------------------------------------------
//LOGICA DEL FORMULARIO DE REGISTRO DE CUENTA
//-------------------------------------------
// Mejorar la experiencia del usuario
const inputs = document.querySelectorAll('.form-input');
inputs.forEach(input => {
    input.addEventListener('focus', function () {
        this.parentNode.style.transform = 'scale(1.02)';
    });

    input.addEventListener('blur', function () {
        this.parentNode.style.transform = 'scale(1)';
    });
});