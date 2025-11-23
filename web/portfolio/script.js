// Try to load Vercel Speed Insights on server (Next.js) and provide a safe browser fallback.
// Usage: await runSpeedInsights('https://example.com', { strategy: 'mobile' })
let SpeedInsights;
if (typeof require === 'function' && typeof module !== 'undefined' && typeof window === 'undefined') {
    try {
        // Attempt CommonJS require for server environments (Next.js)
        // If using ESM on the server, adapt accordingly in your Next.js code (this file is a browser script).
        const mod = require('@vercel/speed-insights/next');
        SpeedInsights = mod && (mod.SpeedInsights || mod.default || mod);
    } catch (e) {
        // Not available on server or not installed — will fall back
        console.warn('SpeedInsights module not available:', e && e.message);
    }
}

/**
 * Run Vercel Speed Insights if available, otherwise return a best-effort client-side metrics object.
 * Defensively handles browser vs server environments.
 * @param {string} url - Page URL to analyze
 * @param {object} [options={}] - Options passed to SpeedInsights when available
 * @returns {Promise<object>} result (lighthouse payload or fallback metrics)
 */
async function runSpeedInsights(url, options = {}) {
    // Server-side: use the installed SpeedInsights integration when possible
    if (typeof SpeedInsights === 'function') {
        try {
            return await SpeedInsights({ url, ...options });
        } catch (err) {
            console.warn('SpeedInsights invocation failed:', err);
            // Fall through to browser fallback where possible
        }
    }

    // Browser fallback: provide lightweight timing metrics using Performance APIs
    if (typeof window !== 'undefined' && typeof performance !== 'undefined') {
        // Wait a tick so navigation timings are populated if called right after load
        await new Promise(resolve => setTimeout(resolve, 0));

        const navigation = (performance.getEntriesByType && performance.getEntriesByType('navigation')[0]) || {};
        const paints = (performance.getEntriesByType && performance.getEntriesByType('paint')) || [];
        const fcp = paints.find(p => p.name === 'first-contentful-paint')?.startTime ?? null;

        // Best-effort LCP if the page collected it into window.__LCP (not guaranteed)
        const lcp = (window.__LCP && window.__LCP.value) || null;

        return {
            source: 'fallback-client',
            url: url || (typeof location !== 'undefined' ? location.href : null),
            metrics: {
                domContentLoaded: navigation.domContentLoadedEventEnd ?? null,
                loadEvent: navigation.loadEventEnd ?? null,
                firstContentfulPaint: fcp,
                largestContentfulPaint: lcp
            }
        };
    }

    // Neither server nor useful browser APIs available
    return {
        error: 'SpeedInsights unavailable in this environment'
    };
}

// Expose function globally for use in the rest of the script
if (typeof window !== 'undefined') {
    window.runSpeedInsights = runSpeedInsights;
}

// Also export for module environments (if this file is imported as a module)
if (typeof module !== 'undefined' && module.exports) {
    module.exports.runSpeedInsights = runSpeedInsights;
}
// Centralized, defensive initialization
document.addEventListener('DOMContentLoaded', () => {
    const oprefersReduced = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    // Robot icon click animation (if present)
    const robotIcon = document.querySelector('.robot-icon');
    if (robotIcon && !prefersReduced) {
        robotIcon.addEventListener('click', function () {
            this.style.animation = 'none';
            setTimeout(() => {
                this.style.animation = 'float 3s ease-in-out infinite';
            }, 10);
        });
    }

    // Navigation mobile (defensive)
    const hamburger = document.querySelector('.hamburger');
    const navMenu = document.querySelector('.nav-menu');
    if (hamburger && navMenu) {
        hamburger.addEventListener('click', () => {
            hamburger.classList.toggle('active');
            navMenu.classList.toggle('active');
        });

        // Fermer le menu mobile au clic sur un lien
        document.querySelectorAll('.nav-link').forEach(n => n.addEventListener('click', () => {
            hamburger.classList.remove('active');
            navMenu.classList.remove('active');
        }));
    }

    // Smooth scroll (only for in-page anchors)
    if (!prefersReduced) {
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                const href = this.getAttribute('href');
                if (!href || href === '#') return;
                const target = document.querySelector(href);
                if (target) {
                    e.preventDefault();
                    target.scrollIntoView({ behavior: 'smooth', block: 'start' });
                }
            });
        });
    }

// Animation des barres de compétences au scroll
const observerOptions = {
    threshold: 0.5,
    rootMargin: '0px 0px -100px 0px'
};

const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            const skillBars = entry.target.querySelectorAll('.skill-progress');
            skillBars.forEach(bar => {
                const width = bar.style.width;
                bar.style.width = '0%';
                setTimeout(() => {
                    bar.style.width = width;
                }, 500);
            });
        }
    });
}, observerOptions);

const skillsSection = document.querySelector('.skills');
if (skillsSection) {
    observer.observe(skillsSection);
}

    // Animation d'apparition des éléments au scroll
    if (!prefersReduced) {
        const fadeInElements = document.querySelectorAll('.skill-card, .project-card, .about-content');
        if (fadeInElements.length) {
            const fadeInObserver = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        entry.target.style.opacity = '1';
                        entry.target.style.transform = 'translateY(0)';
                    }
                });
            }, { threshold: 0.1, rootMargin: '0px 0px -50px 0px' });

            fadeInElements.forEach(el => {
                el.style.opacity = '0';
                el.style.transform = 'translateY(30px)';
                el.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
                fadeInObserver.observe(el);
            });
        }
    }

    // Combine scroll handlers into a single listener for performance
    let lastKnownScrollY = 0;
    let ticking = false;

    function onScroll() {
        lastKnownScrollY = window.pageYOffset;
        if (!ticking) {
            window.requestAnimationFrame(() => {
                const scrolled = lastKnownScrollY;

                // Parallax hero
                const hero = document.querySelector('.hero');
                if (hero && !prefersReduced) {
                    hero.style.transform = `translateY(${scrolled * 0.5}px)`;
                }

                // Navbar color
                const navbar = document.querySelector('.navbar');
                if (navbar) {
                    navbar.style.background = (scrolled > 100) ? 'rgba(15, 23, 42, 0.98)' : 'rgba(15, 23, 42, 0.95)';
                }

                ticking = false;
            });
            ticking = true;
        }
    }

    window.addEventListener('scroll', onScroll, { passive: true });
    // Set initial navbar/hero state immediately to avoid flash
    onScroll();

    // Shared: animation au scroll pour les cartes (remplace les scripts inline présents dans plusieurs pages)
    if (!prefersReduced) {
        try {
            const observerOptionsShared = { threshold: 0.1, rootMargin: '0px 0px -50px 0px' };
            const sharedObserver = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        entry.target.style.opacity = '1';
                        entry.target.style.transform = 'translateY(0)';
                    }
                });
            }, observerOptionsShared);

            document.querySelectorAll('.competence-card, .flow-card, .result-card, .tech-category').forEach(card => {
                card.style.opacity = '0';
                card.style.transform = 'translateY(20px)';
                card.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
                sharedObserver.observe(card);
            });
        } catch (e) {
            console.warn('Shared observer script failed:', e);
        }
    }
});

// Shared: animation au scroll pour les cartes (remplace les scripts inline présents dans plusieurs pages)
document.addEventListener('DOMContentLoaded', () => {
    try {
        const observerOptionsShared = {
            threshold: 0.1,
            rootMargin: '0px 0px -50px 0px'
        };

        const sharedObserver = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.style.opacity = '1';
                    entry.target.style.transform = 'translateY(0)';
                }
            });
        }, observerOptionsShared);

        document.querySelectorAll('.competence-card, .flow-card, .result-card, .tech-category').forEach(card => {
            card.style.opacity = '0';
            card.style.transform = 'translateY(20px)';
            card.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
            sharedObserver.observe(card);
        });
    } catch (e) {
        // Defensive: if DOM APIs are not present, silently skip
        console.warn('Shared observer script failed:', e);
    }
});
