function toggleSidebar() {
    document.querySelector('.main-sidebar').classList.toggle('collapsed');
    document.querySelector('.content-wrapper').classList.toggle('collapsed');
}

// Particle Effect
function createParticles() {
    const particlesContainer = document.getElementById('particles');
    for (let i = 0; i < 20; i++) {
        const particle = document.createElement('div');
        particle.classList.add('particle');
        particle.style.width = `${Math.random() * 3 + 1}px`;
        particle.style.height = particle.style.width;
        particle.style.left = `${Math.random() * 100}%`;
        particle.style.animationDelay = `${Math.random() * 15}s`;
        particle.style.background = `rgba(74, 144, 226, 0.3)`;
        particlesContainer.appendChild(particle);
    }
}

window.onload = createParticles;