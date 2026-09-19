/**
 * WallVerse HD - PWA & Web Client Application
 */

// Register Service Worker for PWA
if ('serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('/sw.js')
      .then(reg => console.log('WallVerse PWA ServiceWorker registered:', reg.scope))
      .catch(err => console.log('WallVerse PWA ServiceWorker registration failed:', err));
  });
}

// Wallpaper Catalog
const WALLPAPERS = [
  {
    id: 'wp_nature_aurora',
    title: 'Emerald Aurora Fjords',
    category: 'Nature',
    categoryEmoji: '🌄',
    resolution: '4K UHD (2160×3840)',
    fileSize: '5.2 MB',
    isPremium: false,
    priceInr: 0,
    isAnimated: false,
    imageSrc: '/assets/wallpapers/img_wp_nature_aurora.jpg',
    downloads: 34500,
    views: 98200,
    author: 'Nordic Lens Studios',
    isFeatured: true,
    isTrending: true
  },
  {
    id: 'wp_cosmic_nebula',
    title: 'Orion Stardust Nebula',
    category: 'Space',
    categoryEmoji: '🌌',
    resolution: '4K UHD (2160×3840)',
    fileSize: '6.1 MB',
    isPremium: true,
    priceInr: 20,
    isAnimated: false,
    imageSrc: '/assets/wallpapers/img_wp_cosmic_nebula.jpg',
    downloads: 48200,
    views: 124000,
    author: 'DeepSky Observatory',
    isFeatured: true,
    isTrending: true
  },
  {
    id: 'wp_cyber_car',
    title: 'Cyberpunk Phantom GT',
    category: 'Cars',
    categoryEmoji: '🚗',
    resolution: '4K UHD (2160×3840)',
    fileSize: '5.8 MB',
    isPremium: true,
    priceInr: 50,
    isAnimated: false,
    imageSrc: '/assets/wallpapers/img_wp_cyber_car.jpg',
    downloads: 28900,
    views: 87400,
    author: 'Apex Concept Design',
    isFeatured: true,
    isTrending: true
  },
  {
    id: 'wp_amoled_dragon',
    title: 'OLED Cyber Dragon',
    category: 'AMOLED',
    categoryEmoji: '🖤',
    resolution: '4K UHD (2160×3840)',
    fileSize: '3.9 MB',
    isPremium: true,
    priceInr: 10,
    isAnimated: false,
    imageSrc: '/assets/wallpapers/img_wp_amoled_dragon.jpg',
    downloads: 51200,
    views: 159000,
    author: 'Monochrome Masters',
    isFeatured: false,
    isTrending: true
  },
  // Live Animated Wallpapers
  {
    id: 'anim_cosmic_vortex',
    title: 'Cosmic Warp & Stardust',
    category: 'Space',
    categoryEmoji: '🌌',
    resolution: '60 FPS Live Canvas',
    fileSize: 'Live Shader',
    isPremium: false,
    priceInr: 0,
    isAnimated: true,
    animType: 'COSMIC_WARP',
    downloads: 63200,
    views: 192000,
    author: 'WallVerse Interactive Lab',
    isFeatured: true,
    isTrending: true
  },
  {
    id: 'anim_neon_aurora',
    title: 'Bioluminescent Aurora Waves',
    category: 'Nature',
    categoryEmoji: '🌄',
    resolution: '60 FPS Live Canvas',
    fileSize: 'Live Shader',
    isPremium: true,
    priceInr: 10,
    isAnimated: true,
    animType: 'AURORA',
    downloads: 41500,
    views: 118000,
    author: 'Fluid Dynamics Art',
    isFeatured: false,
    isTrending: true
  },
  {
    id: 'anim_amoled_matrix',
    title: 'AMOLED Quantum Rain Grid',
    category: 'AMOLED',
    categoryEmoji: '🖤',
    resolution: '60 FPS Live Canvas',
    fileSize: 'Live Shader',
    isPremium: false,
    priceInr: 0,
    isAnimated: true,
    animType: 'AMOLED_MATRIX',
    downloads: 57100,
    views: 175000,
    author: 'OLED Core Interactive',
    isFeatured: false,
    isTrending: true
  },
  {
    id: 'anim_quantum_pulse',
    title: 'Quantum Pulsar Plasma',
    category: 'Abstract',
    categoryEmoji: '🎨',
    resolution: '60 FPS Live Canvas',
    fileSize: 'Live Shader',
    isPremium: true,
    priceInr: 5,
    isAnimated: true,
    animType: 'QUANTUM_PULSE',
    downloads: 22400,
    views: 69000,
    author: 'Plasma Dynamics',
    isFeatured: false,
    isTrending: false
  },
  {
    id: 'anim_fireflies',
    title: 'Mystic Forest Fireflies',
    category: 'Nature',
    categoryEmoji: '🌄',
    resolution: '60 FPS Live Canvas',
    fileSize: 'Live Shader',
    isPremium: false,
    priceInr: 0,
    isAnimated: true,
    animType: 'FIREFLIES',
    downloads: 38900,
    views: 104000,
    author: 'Enchanted Nature Lab',
    isFeatured: false,
    isTrending: false
  },
  // Curated Additional Categories
  {
    id: 'wp_gaming_mecha',
    title: 'Neon Mecha Ronin',
    category: 'Gaming',
    categoryEmoji: '🎮',
    resolution: '4K UHD (2160×3840)',
    fileSize: '4.8 MB',
    isPremium: true,
    priceInr: 20,
    isAnimated: false,
    downloads: 32100,
    views: 94000,
    author: 'CyberPixel Studios',
    primaryColor: '#8b5cf6',
    secondaryColor: '#ec4899',
    isFeatured: false,
    isTrending: true
  },
  {
    id: 'wp_anime_sakura',
    title: 'Spirit Blossom Blade',
    category: 'Anime',
    categoryEmoji: '⚔️',
    resolution: '4K UHD (2160×3840)',
    fileSize: '4.2 MB',
    isPremium: false,
    priceInr: 0,
    isAnimated: false,
    downloads: 68400,
    views: 210000,
    author: 'MangaVerse Arts',
    primaryColor: '#f43f5e',
    secondaryColor: '#fb7185',
    isFeatured: false,
    isTrending: true
  },
  {
    id: 'wp_animals_lion',
    title: 'Golden Serengeti Monarch',
    category: 'Animals',
    categoryEmoji: '🦁',
    resolution: '4K UHD (2160×3840)',
    fileSize: '5.1 MB',
    isPremium: true,
    priceInr: 10,
    isAnimated: false,
    downloads: 29300,
    views: 84000,
    author: 'WildAfrica Visuals',
    primaryColor: '#d97706',
    secondaryColor: '#f59e0b',
    isFeatured: false,
    isTrending: false
  },
  {
    id: 'wp_abstract_prism',
    title: 'Prismatic Obsidian Geometry',
    category: 'Abstract',
    categoryEmoji: '🎨',
    resolution: '4K UHD (2160×3840)',
    fileSize: '4.4 MB',
    isPremium: false,
    priceInr: 0,
    isAnimated: false,
    downloads: 41200,
    views: 129000,
    author: 'Studio Refract',
    primaryColor: '#9333ea',
    secondaryColor: '#38bdf8',
    isFeatured: false,
    isTrending: true
  },
  {
    id: 'wp_festivals_lights',
    title: 'Celestial Lantern Constellation',
    category: 'Festivals',
    categoryEmoji: '🎆',
    resolution: '4K UHD (2160×3840)',
    fileSize: '5.3 MB',
    isPremium: true,
    priceInr: 5,
    isAnimated: false,
    downloads: 19400,
    views: 62000,
    author: 'Global Celebrations Art',
    primaryColor: '#e11d48',
    secondaryColor: '#f59e0b',
    isFeatured: false,
    isTrending: false
  },
  {
    id: 'wp_tech_quantum',
    title: 'Superconducting Quantum Core',
    category: 'Technology',
    categoryEmoji: '⚡',
    resolution: '4K UHD (2160×3840)',
    fileSize: '4.7 MB',
    isPremium: false,
    priceInr: 0,
    isAnimated: false,
    downloads: 36800,
    views: 112000,
    author: 'NanoSilicon Tech',
    primaryColor: '#0284c7',
    secondaryColor: '#06b6d4',
    isFeatured: false,
    isTrending: true
  },
  {
    id: 'wp_movies_interstellar',
    title: 'Gargantua Singularity Event',
    category: 'Movies & Entertainment',
    categoryEmoji: '🎬',
    resolution: '4K UHD (2160×3840)',
    fileSize: '5.6 MB',
    isPremium: true,
    priceInr: 50,
    isAnimated: false,
    downloads: 54000,
    views: 171000,
    author: 'CinemaWarp FX',
    primaryColor: '#4f46e5',
    secondaryColor: '#f59e0b',
    isFeatured: false,
    isTrending: true
  }
];

const CATEGORIES = [
  { title: 'Nature', emoji: '🌄', color: '#059669' },
  { title: 'Cars', emoji: '🚗', color: '#dc2626' },
  { title: 'Gaming', emoji: '🎮', color: '#7c3aed' },
  { title: 'Anime', emoji: '⚔️', color: '#db2777' },
  { title: 'Space', emoji: '🌌', color: '#4338ca' },
  { title: 'Animals', emoji: '🦁', color: '#d97706' },
  { title: 'AMOLED', emoji: '🖤', color: '#1e293b' },
  { title: 'Abstract', emoji: '🎨', color: '#9333ea' },
  { title: 'Festivals', emoji: '🎆', color: '#e11d48' },
  { title: 'Technology', emoji: '⚡', color: '#0284c7' },
  { title: 'Movies & Entertainment', emoji: '🎬', color: '#4f46e5' }
];

class WallVerseApp {
  constructor() {
    this.currentTab = 'home';
    this.currentFilter = 'all';
    this.searchQuery = '';
    this.selectedCategory = null;
    this.activeWallpaper = null;
    this.previewOverlayState = { clock: true, dock: false };
    this.favorites = this.loadFavorites();
    this.purchases = this.loadPurchases();
    this.deferredInstallPrompt = null;
    this.activeAnimationLoop = null;

    this.init();
  }

  loadFavorites() {
    try {
      const stored = localStorage.getItem('wallverse_favorites');
      return stored ? JSON.parse(stored) : ['wp_nature_aurora'];
    } catch {
      return ['wp_nature_aurora'];
    }
  }

  saveFavorites() {
    try {
      localStorage.setItem('wallverse_favorites', JSON.stringify(this.favorites));
    } catch (e) {
      console.error(e);
    }
    this.updateFavBadge();
  }

  loadPurchases() {
    try {
      const stored = localStorage.getItem('wallverse_purchases');
      return stored ? JSON.parse(stored) : [];
    } catch {
      return [];
    }
  }

  savePurchases() {
    try {
      localStorage.setItem('wallverse_purchases', JSON.stringify(this.purchases));
    } catch (e) {
      console.error(e);
    }
  }

  init() {
    this.renderCategoriesRibbon();
    this.renderWallpapers();
    this.updateFavBadge();
    this.setupPwaInstall();
    this.startClock();
  }

  setupPwaInstall() {
    window.addEventListener('beforeinstallprompt', (e) => {
      e.preventDefault();
      this.deferredInstallPrompt = e;
      const btn = document.getElementById('install-pwa-btn');
      if (btn) btn.style.display = 'inline-flex';
    });
  }

  promptPwaInstall() {
    if (this.deferredInstallPrompt) {
      this.deferredInstallPrompt.prompt();
      this.deferredInstallPrompt.userChoice.then((choiceResult) => {
        if (choiceResult.outcome === 'accepted') {
          this.showToast('✨ WallVerse HD installed successfully!');
        }
        this.deferredInstallPrompt = null;
      });
    } else {
      this.showToast('💡 To install: tap Share / Menu -> Add to Home Screen');
    }
  }

  startClock() {
    const updateTime = () => {
      const now = new Date();
      const timeEl = document.getElementById('lockscreen-time');
      const dateEl = document.getElementById('lockscreen-date');
      if (timeEl) {
        timeEl.textContent = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: false });
      }
      if (dateEl) {
        dateEl.textContent = now.toLocaleDateString([], { weekday: 'long', month: 'long', day: 'numeric' });
      }
    };
    updateTime();
    setInterval(updateTime, 1000);
  }

  renderCategoriesRibbon() {
    const container = document.getElementById('categories-scroll');
    if (!container) return;

    container.innerHTML = CATEGORIES.map(cat => `
      <div class="category-badge-card glass" style="border-left: 3px solid ${cat.color};" onclick="app.selectCategory('${cat.title}')">
        <span style="font-size: 1.2rem;">${cat.emoji}</span>
        <span>${cat.title}</span>
      </div>
    `).join('');
  }

  setTab(tab) {
    this.currentTab = tab;

    // Update nav links
    document.querySelectorAll('.nav-link, .bottom-tab').forEach(el => {
      el.classList.toggle('active', el.dataset.tab === tab);
    });

    const hero = document.getElementById('hero-banner');
    const categoriesSection = document.getElementById('categories-section');
    const heading = document.getElementById('gallery-heading');

    if (tab === 'home') {
      if (hero) hero.style.display = 'block';
      if (categoriesSection) categoriesSection.style.display = 'block';
      if (heading) heading.textContent = 'Featured & Trending Wallpapers';
      this.currentFilter = 'all';
      this.selectedCategory = null;
    } else if (tab === 'categories') {
      if (hero) hero.style.display = 'none';
      if (categoriesSection) categoriesSection.style.display = 'block';
      if (heading) heading.textContent = 'Explore All Categories';
      this.currentFilter = 'all';
    } else if (tab === 'favorites') {
      if (hero) hero.style.display = 'none';
      if (categoriesSection) categoriesSection.style.display = 'none';
      if (heading) heading.textContent = 'My Favorites & Saved Collection';
    } else if (tab === 'premium') {
      if (hero) hero.style.display = 'none';
      if (categoriesSection) categoriesSection.style.display = 'none';
      if (heading) heading.textContent = '👑 VIP Masterpiece Collection (₹5 - ₹50)';
      this.currentFilter = 'premium-all';
    }

    this.renderWallpapers();
  }

  selectCategory(category) {
    this.selectedCategory = category;
    const heading = document.getElementById('gallery-heading');
    if (heading) heading.textContent = `${category} Wallpapers`;
    this.renderWallpapers();
    window.scrollTo({ top: 400, behavior: 'smooth' });
  }

  setFilter(filter) {
    this.currentFilter = filter;
    document.querySelectorAll('.filter-chip').forEach(chip => {
      chip.classList.toggle('active', chip.dataset.filter === filter);
    });
    this.renderWallpapers();
  }

  onSearchInput(value) {
    this.searchQuery = value.trim().toLowerCase();
    const clearBtn = document.getElementById('clear-search');
    if (clearBtn) clearBtn.style.display = this.searchQuery ? 'block' : 'none';
    this.renderWallpapers();
  }

  clearSearch() {
    const input = document.getElementById('search-input');
    if (input) input.value = '';
    this.searchQuery = '';
    const clearBtn = document.getElementById('clear-search');
    if (clearBtn) clearBtn.style.display = 'none';
    this.renderWallpapers();
  }

  getFilteredWallpapers() {
    return WALLPAPERS.filter(item => {
      // Tab filtering
      if (this.currentTab === 'favorites') {
        if (!this.favorites.includes(item.id)) return false;
      }
      if (this.currentTab === 'premium') {
        if (!item.isPremium) return false;
      }

      // Category filter
      if (this.selectedCategory && item.category !== this.selectedCategory) {
        return false;
      }

      // Chip Filter
      if (this.currentFilter === '4k' && item.isAnimated) return false;
      if (this.currentFilter === 'animated' && !item.isAnimated) return false;
      if (this.currentFilter === 'free' && item.isPremium) return false;
      if (this.currentFilter === 'premium-all' && !item.isPremium) return false;
      if (this.currentFilter === 'price-5' && item.priceInr !== 5) return false;
      if (this.currentFilter === 'price-10' && item.priceInr !== 10) return false;
      if (this.currentFilter === 'price-20' && item.priceInr !== 20) return false;
      if (this.currentFilter === 'price-50' && item.priceInr !== 50) return false;

      // Search Query
      if (this.searchQuery) {
        const matchTitle = item.title.toLowerCase().includes(this.searchQuery);
        const matchCat = item.category.toLowerCase().includes(this.searchQuery);
        const matchRes = item.resolution.toLowerCase().includes(this.searchQuery);
        const matchPrice = item.isPremium ? (`₹${item.priceInr}`).includes(this.searchQuery) : 'free'.includes(this.searchQuery);
        return matchTitle || matchCat || matchRes || matchPrice;
      }

      return true;
    });
  }

  renderWallpapers() {
    const grid = document.getElementById('wallpaper-grid');
    const countLabel = document.getElementById('results-count-label');
    if (!grid) return;

    const items = this.getFilteredWallpapers();
    if (countLabel) {
      countLabel.textContent = `${items.length} Wallpapers Available`;
    }

    if (items.length === 0) {
      grid.innerHTML = `
        <div style="grid-column: 1 / -1; text-align: center; padding: 60px 20px;">
          <div style="font-size: 3rem; margin-bottom: 12px;">🔍</div>
          <h3 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 6px;">No Wallpapers Found</h3>
          <p style="color: var(--text-secondary); font-size: 0.85rem;">Try adjusting your search query or price tier filter.</p>
        </div>
      `;
      return;
    }

    grid.innerHTML = items.map(wp => {
      const isFav = this.favorites.includes(wp.id);
      const isPurchased = this.purchases.includes(wp.id);

      let priceBadgeHtml = '';
      if (!wp.isPremium) {
        priceBadgeHtml = `<span class="badge-price free">FREE</span>`;
      } else if (isPurchased) {
        priceBadgeHtml = `<span class="badge-price unlocked">✓ UNLOCKED</span>`;
      } else {
        const pClass = `p-${wp.priceInr}`;
        priceBadgeHtml = `<span class="badge-price ${pClass}">👑 ₹${wp.priceInr}</span>`;
      }

      const resBadgeHtml = wp.isAnimated 
        ? `<span class="badge-live">⚡ LIVE</span>` 
        : `<span class="badge-res">4K UHD</span>`;

      // Visual Background: Either image or animated canvas gradient placeholder
      let mediaHtml = '';
      if (wp.imageSrc) {
        mediaHtml = `<img src="${wp.imageSrc}" alt="${wp.title}" class="wp-card-media" loading="lazy">`;
      } else {
        const bgGrad = `linear-gradient(145deg, ${wp.primaryColor || '#7c3aed'}, ${wp.secondaryColor || '#06b6d4'})`;
        mediaHtml = `
          <div class="wp-card-media" style="background:${bgGrad}; display:flex; align-items:center; justify-content:center;">
            <span style="font-size:2.8rem; filter:drop-shadow(0 4px 12px rgba(0,0,0,0.5));">${wp.categoryEmoji || '✨'}</span>
          </div>
        `;
      }

      return `
        <div class="wp-card" onclick="app.openPreview('${wp.id}')">
          ${mediaHtml}
          <div class="wp-card-gradient"></div>
          
          <div class="wp-top-badges">
            ${resBadgeHtml}
            <div style="display:flex; align-items:center; gap:6px;">
              ${priceBadgeHtml}
              <button class="fav-btn ${isFav ? 'favorited' : ''}" onclick="event.stopPropagation(); app.toggleFavorite('${wp.id}')">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="${isFav ? '#ef4444' : 'none'}" stroke="currentColor" stroke-width="2"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path></svg>
              </button>
            </div>
          </div>

          <div class="wp-bottom-info">
            <div class="wp-title">${wp.title}</div>
            <div class="wp-meta-row">
              <span>${wp.category}</span>
              <span>↓ ${(wp.downloads / 1000).toFixed(1)}k</span>
            </div>
          </div>
        </div>
      `;
    }).join('');
  }

  toggleFavorite(id) {
    if (this.favorites.includes(id)) {
      this.favorites = this.favorites.filter(x => x !== id);
      this.showToast('Removed from Favorites');
    } else {
      this.favorites.push(id);
      this.showToast('❤️ Added to Favorites');
    }
    this.saveFavorites();
    this.renderWallpapers();
    if (this.activeWallpaper && this.activeWallpaper.id === id) {
      this.updatePreviewFavBtn();
    }
  }

  toggleCurrentFavorite() {
    if (this.activeWallpaper) {
      this.toggleFavorite(this.activeWallpaper.id);
    }
  }

  updateFavBadge() {
    const badge = document.getElementById('fav-count-badge');
    if (badge) badge.textContent = this.favorites.length;
  }

  // Fullscreen Preview Modal
  openPreview(id) {
    const wp = WALLPAPERS.find(w => w.id === id);
    if (!wp) return;
    this.activeWallpaper = wp;

    const modal = document.getElementById('preview-modal');
    const img = document.getElementById('modal-img');
    const canvas = document.getElementById('modal-canvas');
    const title = document.getElementById('preview-title');
    const subtitle = document.getElementById('preview-subtitle');
    const resPill = document.getElementById('preview-res-pill');
    const mainActionBtn = document.getElementById('preview-main-action');
    const btnText = document.getElementById('preview-btn-text');

    if (title) title.textContent = wp.title;
    if (subtitle) subtitle.textContent = `${wp.category} • ${wp.fileSize} • ${(wp.downloads / 1000).toFixed(1)}K Downloads • ${wp.author}`;
    if (resPill) resPill.textContent = wp.resolution;

    // Check purchase status
    const isPurchased = this.purchases.includes(wp.id);
    if (wp.isPremium && !isPurchased) {
      if (btnText) btnText.textContent = `Unlock 4K Original (₹${wp.priceInr})`;
      if (mainActionBtn) mainActionBtn.style.background = 'linear-gradient(135deg, #f59e0b, #d97706)';
    } else {
      if (btnText) btnText.textContent = wp.isAnimated ? 'Download Live Shader' : 'Download 4K HD';
      if (mainActionBtn) mainActionBtn.style.background = 'linear-gradient(135deg, #7c3aed, #4f46e5)';
    }

    // Handle Media: Animated Canvas vs Static Image
    if (this.activeAnimationLoop) {
      cancelAnimationFrame(this.activeAnimationLoop);
      this.activeAnimationLoop = null;
    }

    if (wp.isAnimated) {
      if (img) img.style.display = 'none';
      if (canvas) {
        canvas.style.display = 'block';
        this.runLiveCanvasAnimation(canvas, wp.animType);
      }
    } else if (wp.imageSrc) {
      if (canvas) canvas.style.display = 'none';
      if (img) {
        img.style.display = 'block';
        img.src = wp.imageSrc;
      }
    } else {
      // Fallback procedural canvas render
      if (img) img.style.display = 'none';
      if (canvas) {
        canvas.style.display = 'block';
        this.renderProceduralStill(canvas, wp);
      }
    }

    this.updatePreviewFavBtn();
    if (modal) modal.classList.add('active');
  }

  closePreview() {
    const modal = document.getElementById('preview-modal');
    if (modal) modal.classList.remove('active');
    if (this.activeAnimationLoop) {
      cancelAnimationFrame(this.activeAnimationLoop);
      this.activeAnimationLoop = null;
    }
  }

  togglePreviewOverlay(type) {
    if (type === 'clock') {
      this.previewOverlayState.clock = !this.previewOverlayState.clock;
      const el = document.getElementById('lockscreen-overlay');
      if (el) el.style.display = this.previewOverlayState.clock ? 'block' : 'none';
    } else if (type === 'dock') {
      this.previewOverlayState.dock = !this.previewOverlayState.dock;
      const el = document.getElementById('dock-overlay');
      if (el) el.style.display = this.previewOverlayState.dock ? 'flex' : 'none';
    }
  }

  updatePreviewFavBtn() {
    const btn = document.getElementById('preview-fav-btn');
    if (!btn || !this.activeWallpaper) return;
    const isFav = this.favorites.includes(this.activeWallpaper.id);
    btn.innerHTML = `<svg width="20" height="20" viewBox="0 0 24 24" fill="${isFav ? '#ef4444' : 'none'}" stroke="${isFav ? '#ef4444' : 'currentColor'}" stroke-width="2"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path></svg>`;
  }

  onPreviewMainAction() {
    if (!this.activeWallpaper) return;
    const wp = this.activeWallpaper;
    const isPurchased = this.purchases.includes(wp.id);

    if (wp.isPremium && !isPurchased) {
      // Open Purchase Modal
      this.openPurchaseModal(wp);
    } else {
      // Trigger High-Res Download
      this.downloadWallpaper(wp);
    }
  }

  openPurchaseModal(wp) {
    const modal = document.getElementById('purchase-modal');
    const desc = document.getElementById('purchase-item-desc');
    if (desc) desc.textContent = `Unlock ${wp.title} (${wp.resolution}) for ₹${wp.priceInr}`;
    if (modal) modal.classList.add('active');
  }

  closePurchaseModal() {
    const modal = document.getElementById('purchase-modal');
    if (modal) modal.classList.remove('active');
  }

  completePurchase(method) {
    if (!this.activeWallpaper) return;
    const wp = this.activeWallpaper;
    this.purchases.push(wp.id);
    this.savePurchases();
    this.closePurchaseModal();
    this.showToast(`🎉 Unlocked with ${method}! Permanent license granted.`);
    this.openPreview(wp.id); // Refresh preview with download button
    this.renderWallpapers();
  }

  downloadWallpaper(wp) {
    this.showToast(`⬇️ Preparing 4K Ultra HD download for ${wp.title}...`);

    setTimeout(() => {
      // Create offscreen high-res canvas or download image source
      const link = document.createElement('a');
      link.download = `${wp.title.replace(/\s+/g, '_')}_4K_WallVerse.jpg`;

      if (wp.imageSrc) {
        link.href = wp.imageSrc;
      } else {
        // Generate high quality canvas export
        const exportCanvas = document.createElement('canvas');
        exportCanvas.width = 2160;
        exportCanvas.height = 3840;
        const ctx = exportCanvas.getContext('2d');
        const grad = ctx.createLinearGradient(0, 0, 0, 3840);
        grad.addColorStop(0, wp.primaryColor || '#1e1b4b');
        grad.addColorStop(1, wp.secondaryColor || '#0284c7');
        ctx.fillStyle = grad;
        ctx.fillRect(0, 0, 2160, 3840);

        // Watermark / Brand stamp
        ctx.fillStyle = 'rgba(255, 255, 255, 0.8)';
        ctx.font = 'bold 64px sans-serif';
        ctx.textAlign = 'center';
        ctx.fillText(wp.title, 1080, 1920);
        ctx.font = '36px sans-serif';
        ctx.fillText('WallVerse HD 4K Original', 1080, 2000);

        link.href = exportCanvas.toDataURL('image/jpeg', 0.95);
      }

      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);

      this.showToast('✅ 4K Wallpaper saved to your device!');
    }, 800);
  }

  shareWallpaper() {
    if (!this.activeWallpaper) return;
    const wp = this.activeWallpaper;
    if (navigator.share) {
      navigator.share({
        title: `WallVerse HD: ${wp.title}`,
        text: `Check out this stunning ${wp.resolution} wallpaper: ${wp.title}`,
        url: window.location.href
      }).catch(() => {});
    } else {
      navigator.clipboard.writeText(window.location.href);
      this.showToast('📋 Link copied to clipboard!');
    }
  }

  // Interactive Live Canvas Animation Shaders
  runLiveCanvasAnimation(canvas, type) {
    const ctx = canvas.getContext('2d');
    canvas.width = canvas.parentElement.clientWidth || 360;
    canvas.height = canvas.parentElement.clientHeight || 640;

    let particles = [];
    const count = 75;
    for (let i = 0; i < count; i++) {
      particles.push({
        x: Math.random() * canvas.width,
        y: Math.random() * canvas.height,
        vx: (Math.random() - 0.5) * 1.5,
        vy: (Math.random() - 0.5) * 1.5,
        radius: Math.random() * 3 + 1,
        color: ['#8b5cf6', '#06b6d4', '#f59e0b', '#ec4899'][Math.floor(Math.random() * 4)],
        pulse: Math.random() * Math.PI
      });
    }

    let t = 0;
    const animate = () => {
      t += 0.02;
      ctx.fillStyle = 'rgba(9, 10, 16, 0.25)';
      ctx.fillRect(0, 0, canvas.width, canvas.height);

      if (type === 'AURORA') {
        for (let i = 0; i < 3; i++) {
          ctx.beginPath();
          ctx.moveTo(0, canvas.height * 0.4 + i * 40);
          for (let x = 0; x <= canvas.width; x += 10) {
            const y = canvas.height * 0.4 + i * 40 + Math.sin(x * 0.01 + t + i) * 50;
            ctx.lineTo(x, y);
          }
          ctx.strokeStyle = i === 0 ? 'rgba(16, 185, 129, 0.4)' : 'rgba(6, 182, 212, 0.3)';
          ctx.lineWidth = 24;
          ctx.filter = 'blur(10px)';
          ctx.stroke();
          ctx.filter = 'none';
        }
      } else if (type === 'AMOLED_MATRIX') {
        ctx.fillStyle = '#06b6d4';
        ctx.font = '12px monospace';
        for (let i = 0; i < particles.length; i++) {
          const p = particles[i];
          ctx.fillText(Math.random() > 0.5 ? '1' : '0', p.x, p.y);
          p.y += 3;
          if (p.y > canvas.height) p.y = 0;
        }
      } else {
        // Cosmic Warp & Particles
        particles.forEach(p => {
          p.x += p.vx;
          p.y += p.vy;
          if (p.x < 0) p.x = canvas.width;
          if (p.x > canvas.width) p.x = 0;
          if (p.y < 0) p.y = canvas.height;
          if (p.y > canvas.height) p.y = 0;

          ctx.beginPath();
          ctx.arc(p.x, p.y, p.radius + Math.sin(t + p.pulse), 0, Math.PI * 2);
          ctx.fillStyle = p.color;
          ctx.fill();
        });
      }

      this.activeAnimationLoop = requestAnimationFrame(animate);
    };

    animate();
  }

  renderProceduralStill(canvas, wp) {
    const ctx = canvas.getContext('2d');
    canvas.width = canvas.parentElement.clientWidth || 360;
    canvas.height = canvas.parentElement.clientHeight || 640;

    const grad = ctx.createLinearGradient(0, 0, canvas.width, canvas.height);
    grad.addColorStop(0, wp.primaryColor || '#1e1b4b');
    grad.addColorStop(1, wp.secondaryColor || '#0284c7');
    ctx.fillStyle = grad;
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    ctx.fillStyle = 'white';
    ctx.font = 'bold 24px sans-serif';
    ctx.textAlign = 'center';
    ctx.fillText(wp.title, canvas.width / 2, canvas.height / 2);
  }

  toggleThemeInfo() {
    this.showToast('🚀 WallVerse HD PWA • 4K UHD & 60 FPS Live Wallpapers');
  }

  showToast(msg) {
    const toast = document.getElementById('app-toast');
    const msgEl = document.getElementById('toast-message');
    if (!toast || !msgEl) return;
    msgEl.textContent = msg;
    toast.classList.add('show');
    clearTimeout(this.toastTimeout);
    this.toastTimeout = setTimeout(() => {
      toast.classList.remove('show');
    }, 2500);
  }
}

// Instantiate App
window.app = new WallVerseApp();
