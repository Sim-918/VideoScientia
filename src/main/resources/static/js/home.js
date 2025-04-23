    document.addEventListener('DOMContentLoaded', function () {
    // Toast 초기화
    const toastEl = document.getElementById('toast');
    const toast = new bootstrap.Toast(toastEl);

    // 좋아요/즐겨찾기 상태 관리 객체
    const contentState = {};

    // 영화 슬라이더 초기화
    new Glide('.glide', {
      type: 'carousel',
      perView: 4,
      gap: 20,
      breakpoints: {
        1200: {
          perView: 3
        },
        768: {
          perView: 2
        },
        576: {
          perView: 1
        }
      }
    }).mount();

    // TV를 fetch로 불러오고 html을 나중에 추가하기 때문에 registerContentActions로 재호출
    function registerContentActions(scope = document) {
      scope.querySelectorAll('.like-btn').forEach(button => {
        button.removeEventListener('click', handleLikeClick); // 중복 방지
        button.addEventListener('click', handleLikeClick);
        const card = button.closest('.card');
        const title = card.querySelector('.card-title').textContent.trim();
        button.dataset.contentId = card.querySelector('img').alt.replace(/\s+/g, '-').toLowerCase();
        button.dataset.contentTitle = title;
      });

      scope.querySelectorAll('.favorite-btn').forEach(button => {
        button.removeEventListener('click', handleFavoriteClick); // 중복 방지
        button.addEventListener('click', handleFavoriteClick);
        const card = button.closest('.card');
        const title = card.querySelector('.card-title').textContent.trim();
        button.dataset.contentId = card.querySelector('img').alt.replace(/\s+/g, '-').toLowerCase();
        button.dataset.contentTitle = title;
      });
    }


    // 좋아요 버튼 클릭 핸들러
    function handleLikeClick(e) {
      const button = e.currentTarget;
      const isActive = button.classList.contains('active');
      const contentId = button.dataset.contentId || 'featured';
      const contentTitle = button.dataset.contentTitle || 'Inception';

      // 상태 업데이트
      contentState[contentId] = contentState[contentId] || {};
      contentState[contentId].liked = !isActive;

      // UI 업데이트
      button.classList.toggle('active');
      const icon = button.querySelector('i');

      if (contentState[contentId].liked) {
        icon.classList.remove('bi-heart');
        icon.classList.add('bi-heart-fill');
      } else {
        icon.classList.remove('bi-heart-fill');
        icon.classList.add('bi-heart');
      }

      // 토스트 메시지 표시
      showToast(
        contentState[contentId].liked ? '💖' : '💔',
        contentState[contentId].liked
          ? `좋아요`
          : `좋아요 취소`,
        'success'
      );
    }

    // 즐겨찾기 버튼 클릭 핸들러
    function handleFavoriteClick(e) {
      const button = e.currentTarget;
      const isActive = button.classList.contains('active');
      const contentId = button.dataset.contentId || 'featured';
      const contentTitle = button.dataset.contentTitle || 'Inception';

      // 상태 업데이트
      contentState[contentId] = contentState[contentId] || {};
      contentState[contentId].favorited = !isActive;

      // UI 업데이트
      button.classList.toggle('active');
      const icon = button.querySelector('i');

      if (contentState[contentId].favorited) {
        icon.classList.remove('bi-star');
        icon.classList.add('bi-star-fill');
      } else {
        icon.classList.remove('bi-star-fill');
        icon.classList.add('bi-star');
      }

      // 토스트 메시지 표시
      showToast(
        contentState[contentId].favorited ? '⭐' : '💥',
        contentState[contentId].favorited
          ? `"${contentTitle}"를 즐겨찾기 목록에 추가했습니다.`
          : `"${contentTitle}"를 즐겨찾기 목록에서 제거했습니다.`,
        'success'
      );
    }

    // 토스트 메시지 표시 함수
    function showToast(title, message, type) {
      const toastTitle = document.getElementById('toastTitle');
      const toastMessage = document.getElementById('toastMessage');
      const toastHeader = toastEl.querySelector('.toast-header');

      // 헤더 클래스 초기화
      toastHeader.className = 'toast-header';

      // 타입에 따른 스타일 적용
      if (type === 'success') {
        toastHeader.classList.add('bg-primary', 'text-white');
      } else if (type === 'error') {
        toastHeader.classList.add('bg-danger', 'text-white');
      } else {
        toastHeader.classList.add('bg-secondary', 'text-white');
      }

      // 내용 설정
      toastTitle.textContent = title;
      toastMessage.textContent = message;

      // 토스트 표시
      toast.show();
    }

    // 이벤트 리스너 등록
    document.querySelectorAll('.like-btn').forEach(button => {
      button.addEventListener('click', handleLikeClick);
      // 데이터 속성 설정 (슬라이드 아이템의 경우)
      if (button.closest('.glide__slide')) {
        const card = button.closest('.card');
        const title = card.querySelector('.card-title').textContent.replace('🎬', '').trim();
        button.dataset.contentId = card.querySelector('img').alt.replace(/\s+/g, '-').toLowerCase();
        button.dataset.contentTitle = title;
      }
    });

    document.querySelectorAll('.favorite-btn').forEach(button => {
      button.addEventListener('click', handleFavoriteClick);
      // 데이터 속성 설정 (슬라이드 아이템의 경우)
      if (button.closest('.glide__slide')) {
        const card = button.closest('.card');
        const title = card.querySelector('.card-title').textContent.replace('🎬', '').trim();
        button.dataset.contentId = card.querySelector('img').alt.replace(/\s+/g, '-').toLowerCase();
        button.dataset.contentTitle = title;
      }
    });
    // TV 탭 클릭시
    let tvLoaded = false;
    document.querySelectorAll('#contentTabs .nav-link').forEach(tab => {
      tab.addEventListener('click', async function () {
        const selected = this.getAttribute('data-tab');
        document.querySelectorAll('#contentTabs .nav-link').forEach(t => t.classList.remove('active'));
        this.classList.add('active');

        document.getElementById('movie-content').style.display = selected === 'movie' ? 'block' : 'none';
        document.getElementById('tv-content').style.display = selected === 'tv' ? 'block' : 'none';

        if (selected === 'tv' && !tvLoaded) {
          try {
            const res = await fetch('/popularTV');
            const tvList = await res.json();

            if (!tvList.length) {
              document.getElementById('tv-slides').innerHTML = '<p class="text-muted">TV 콘텐츠가 없습니다.</p>';
              return;
            }

            const html = tvList.map(item => `
  <li class="glide__slide">
      <div class="card h-100">
          <img src="https://image.tmdb.org/t/p/w300${item.posterPath}" class="card-img-top" alt="${item.name}">
          <div class="card-body">
              <h5 class="card-title"><i class="bi bi-tv"></i> ${item.name}</h5>
              <p class="card-text text-muted">평점: ${item.voteAverage}</p>
              <div class="content-actions">
                  <button class="btn btn-outline-secondary btn-sm like-btn">
                      <i class="bi bi-heart me-1"></i>
                  </button>
                  <button class="btn btn-outline-secondary btn-sm favorite-btn">
                      <i class="bi bi-star me-1"></i>
                  </button>
                  <a href="/movie/1" class="btn btn-outline-primary btn-sm ms-auto">
                      <i class="bi bi-chevron-right"></i>
                  </a>
              </div>
          </div>
      </div>
  </li>
  `).join('');

            document.getElementById('tv-slides').innerHTML = html;
            // 좋아요, 즐겨찾기 fetch로 인한 재호출
            registerContentActions(document.getElementById('tv-slides'));

            setTimeout(() => new Glide('#tv-content', {
              type: 'carousel',
              perView: 4,
              gap: 20,
              breakpoints: {
                1200: { perView: 3 },
                768: { perView: 2 },
                576: { perView: 1 }
              }
            }).mount());
            tvLoaded = true;

          } catch (e) {
            document.getElementById('tv-slides').innerHTML = '<p class="text-danger">TV 데이터를 불러올 수 없습니다.</p>';
            console.error(e);
          }
        }
      });
    });

    // 검색 기능
    const searchInput = document.querySelector('.search-bar input');
    const searchButton = document.querySelector('.search-bar button');

    searchButton.addEventListener('click', function () {
      const searchTerm = searchInput.value.trim();
      if (searchTerm) {
        showToast('검색 실행', `"${searchTerm}" 검색을 실행합니다.`, 'info');
        // 실제로는 검색 결과 페이지로 이동하거나 AJAX로 결과를 가져옵니다.
      }
    });

    // 엔터 키로 검색
    searchInput.addEventListener('keypress', function (e) {
      if (e.key === 'Enter') {
        searchButton.click();
      }
    });
  });
