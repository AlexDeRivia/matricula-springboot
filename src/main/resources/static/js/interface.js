document.addEventListener('DOMContentLoaded', () => {
    const menu = document.getElementById('primaryNav');
    const menuButton = document.querySelector('.nav-toggle');
    if (menu && menuButton) {
        menu.addEventListener('shown.bs.collapse', () => menuButton.setAttribute('aria-label', 'Cerrar menú'));
        menu.addEventListener('hidden.bs.collapse', () => menuButton.setAttribute('aria-label', 'Abrir menú'));
    }

    const path = window.location.pathname;
    document.querySelectorAll('.site-nav a').forEach((link) => {
        const section = new URL(link.href).pathname.split('/')[1];
        if (path.startsWith(`/${section}/`) || path === `/${section}` ||
            (section === 'alumno' && path.startsWith('/nota/'))) {
            link.setAttribute('aria-current', 'page');
        }
    });

    document.querySelectorAll('form[data-progress]').forEach((form) => {
        form.addEventListener('submit', () => {
            const button = form.querySelector('button[type="submit"]');
            if (!button || button.disabled) return;
            button.dataset.originalLabel = button.textContent;
            button.textContent = button.dataset.loadingLabel || 'Guardando…';
            button.disabled = true;
            button.setAttribute('aria-busy', 'true');
        });
    });

    const firstInvalid = document.querySelector('[aria-invalid="true"]');
    if (firstInvalid) firstInvalid.focus({ preventScroll: true });
});

window.addEventListener('pageshow', () => {
    document.querySelectorAll('button[data-original-label]').forEach((button) => {
        button.textContent = button.dataset.originalLabel;
        button.disabled = false;
        button.removeAttribute('aria-busy');
    });
});
