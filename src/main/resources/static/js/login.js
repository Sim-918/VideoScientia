
document.addEventListener('DOMContentLoaded', () => {
    //요소 가져옴
    const form = document.getElementById('loginForm');
    const submitBtn = document.getElementById('submitBtn');

    const toast = document.getElementById('toast');
    const toastHeader = document.getElementById('toastHeader');
    const toastBody = document.getElementById('toastBody');

    let isLoading = false;  //중복 제출 방지

    // 폼 제출 fetch
    form.addEventListener('submit', async (e) => {
            e.preventDefault();
            if (isLoading) return;

            isLoading = true;
            submitBtn.disabled = true;
            submitBtn.innerHTML = `<span class="inline-block animate-spin rounded-full h-4 w-4 border-2 border-white border-t-transparent mr-2"></span>로그인 중...`;

            const formData = new URLSearchParams();
            formData.append('username', form.userId.value);
            formData.append('password', form.password.value);

            try {
            //로그인 요청 보내기
                const response = await fetch('/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: formData
                });

                if (response.ok) {
                //로그인 성공
                    //showToast('로그인 성공', '환영합니다! 성공적으로 로그인되었습니다.', 'success');
                    setTimeout(() => window.location.href = '/', 500);
                } else {
                //로그인 실패
                    const resJson = await response.json();
                    showToast('로그인 실패', resJson.message || '아이디 또는 비밀번호를 확인해주세요.', 'error');
                }
            } catch (err) {
                // 서버 통신 에러
                showToast('오류', '서버와 통신에 실패했습니다.', 'error');
            } finally {
                //요청 완료 후 버튼 상태 복구
                isLoading = false;
                submitBtn.disabled = false;
                submitBtn.textContent = '로그인';
            }
        });

    function showToast(title, message, type) {
        toastHeader.textContent = title;
        toastBody.textContent = message;

        toastHeader.className = 'px-4 py-2 border-b font-medium rounded-t-lg';
        if (type === 'success') {
            toastHeader.classList.add('bg-blue-600', 'text-white');
        } else {
            toastHeader.classList.add('bg-red-600', 'text-white');
        }

        toast.classList.remove('opacity-0', 'translate-y-4', 'pointer-events-none');
        toast.classList.add('opacity-100', 'translate-y-0');

        setTimeout(() => {
            toast.classList.add('opacity-0', 'translate-y-4', 'pointer-events-none');
            toast.classList.remove('opacity-100', 'translate-y-0');
        }, 3000);
    }
});
