$(function () {
  // 현재 연도를 기준으로 출생년도 선택 옵션 생성
  const currentYear = new Date().getFullYear();
  for (let year = currentYear; year >= 1900; year--) {
    const option = $("<option>").val(year).text(`${year}년`);
    $("#birthYear").append(option);
  }

  // 출생연도 선택 시 자동으로 나이 계산
  $("#birthYear").on("change", function () {
    const year = parseInt($(this).val(), 10);
    if (!isNaN(year)) {
      $("#age").val(currentYear - year + 1); // 한국식 나이 계산
    }
  });

  // 전화번호 입력 시 자동으로 하이픈(-) 삽입
  $("#phoneNum").on("input", function () {
    let value = $(this).val().replace(/[^\d]/g, ""); // 숫자 이외 문자 제거
    if (value.length > 3 && value.length <= 7) {
      value = value.slice(0, 3) + "-" + value.slice(3);
    } else if (value.length > 7) {
      value = value.slice(0, 3) + "-" + value.slice(3, 7) + "-" + value.slice(7, 11);
    }
    $(this).val(value);
  });

  // 입력값 유효성 검사용 정규식 모음
  const validate = {
    userId: v => /^[a-zA-Z0-9_]{4,20}$/.test(v), // 영문, 숫자, 언더스코어 4~20자
    email: v => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v), // 이메일 기본 형식
    password: v => /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/.test(v), // 대소문자+숫자+특수문자+8자 이상
    confirmPassword: (v, p) => v === p, // 비밀번호 일치 확인
    gen: v => !!v, // 성별 선택 여부
    birthYear: v => !!v, // 출생년도 선택 여부
    phoneNum: v => /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/.test(v) // 전화번호 형식
  };

  // 각 필드 유효성 여부 저장 변수
  let userIdValid = false, emailValid = false;
  let passwordValid = false, confirmPasswordValid = false;
  let phoneNumValid = false, genValid = false, birthYearValid = false;

  // 모든 필드 유효할 때 제출 버튼 활성화
  function toggleSubmit() {
    const allValid = userIdValid && emailValid && passwordValid && confirmPasswordValid && phoneNumValid && genValid && birthYearValid;
    const $submitBtn = $("#submitBtn");
    $submitBtn.prop("disabled", !allValid);

    if (allValid) {
      $submitBtn.removeClass("cursor-not-allowed bg-blue-400").addClass("cursor-pointer bg-blue-700");
    } else {
      $submitBtn.removeClass("cursor-pointer bg-blue-700").addClass("cursor-not-allowed bg-blue-400");
    }
  }

  // 사용자에게 메시지를 토스트 형태로 출력
  function showToast(message, type = "success") {
    const toast = $("#toast");

    toast.removeClass("bg-green-600 bg-red-600 hidden").addClass(type === "success" ? "bg-green-600" : "bg-red-600");
    toast.text(message);
    toast.addClass("opacity-100").removeClass("opacity-0");

    setTimeout(() => {
      toast.removeClass("opacity-100").addClass("opacity-0");
    }, 3000);
    setTimeout(() => {
      toast.addClass("hidden");
    }, 3500);
  }

  // 아이디/이메일 중복 확인 API 요청
  function checkDuplicate(field, value, $errorEl, isFormatValid, formatMsg, existsMsg, availableMsg) {
    if (!isFormatValid(value)) {
      $errorEl.text(formatMsg).removeClass("text-green-600 hidden").addClass("text-red-600");
      if (field === "userId") userIdValid = false;
      if (field === "email") emailValid = false;
      toggleSubmit();
      return;
    }

    const path = field === "userId" ? "check-id" : "check-email";

    fetch(`/api/${path}?value=${encodeURIComponent(value)}`, {
      method: "GET",
      headers: { "Accept": "application/json" }
    })
      .then(response => {
        if (!response.ok) throw new Error("Network response was not ok");
        return response.json();
      })
      .then(res => {
        if (res.exists) {
          $errorEl.text(existsMsg).removeClass("text-green-600 hidden").addClass("text-red-600");
          if (field === "userId") userIdValid = false;
          if (field === "email") emailValid = false;
        } else {
          $errorEl.text(availableMsg).removeClass("text-red-600 hidden").addClass("text-green-600");
          if (field === "userId") userIdValid = true;
          if (field === "email") emailValid = true;
        }
        toggleSubmit();
      })
      .catch(() => {
        $errorEl.text("서버 오류가 발생했습니다.").addClass("text-red-600").removeClass("hidden");
        if (field === "userId") userIdValid = false;
        if (field === "email") emailValid = false;
        toggleSubmit();
      });
  }

  // 입력 시 중복 검사 딜레이 타이머 관리
  let debounceTimers = {};

  // 중복 검사 지연 호출 (디바운싱)
  function debounceCheck(field, value, $errorEl, isFormatValid, formatMsg, existsMsg, availableMsg) {
    clearTimeout(debounceTimers[field]);

    debounceTimers[field] = setTimeout(() => {
      checkDuplicate(field, value, $errorEl, isFormatValid, formatMsg, existsMsg, availableMsg);
    }, 500); // 0.5초 대기
  }

  // 아이디 입력 이벤트 처리
  $("#userId").on("input", function () {
    const val = $(this).val().trim();
    debounceCheck("userId", val, $("#userIdError"), validate.userId,
      "4~20자의 영문/숫자/언더스코어만 사용 가능합니다.",
      "이미 존재하는 아이디입니다.",
      "사용가능한 아이디입니다.");
  });

  // 이메일 입력 이벤트 처리
  $("#email").on("input", function () {
    const val = $(this).val().trim();
    debounceCheck("email", val, $("#emailError"), validate.email,
      "올바른 이메일 주소를 입력해주세요.",
      "이미 존재하는 이메일입니다.",
      "사용가능한 이메일입니다.");
  });

  // 비밀번호 입력 시 유효성 검사
  $("#password").on("input", function () {
    const val = $(this).val();
    const $err = $("#passwordError");

    if (!validate.password(val)) {
      $err.text("영문 대/소문자, 숫자, 특수문자를 포함해 8자 이상 입력해주세요.").removeClass("text-green-600 hidden").addClass("text-red-600");
      passwordValid = false;
    } else {
      $err.text("사용가능한 비밀번호입니다.").removeClass("text-red-600 hidden").addClass("text-green-600");
      passwordValid = true;
    }
    toggleSubmit();
  });

  // 비밀번호 확인 입력 시 일치 여부 검사
  $("#confirmPassword").on("input", function () {
    const val = $(this).val();
    const passwordVal = $("#password").val();
    const $err = $("#confirmPasswordError");

    if (!validate.confirmPassword(val, passwordVal)) {
      $err.text("비밀번호가 일치하지 않습니다.").removeClass("text-green-600 hidden").addClass("text-red-600");
      confirmPasswordValid = false;
    } else {
      $err.text("비밀번호가 일치합니다.").removeClass("text-red-600 hidden").addClass("text-green-600");
      confirmPasswordValid = true;
    }
    toggleSubmit();
  });

  // 전화번호 입력 종료 후 유효성 검사
  $("#phoneNum").on("blur", function () {
    const val = $(this).val();
    const $err = $("#phoneNumError");

    if (!validate.phoneNum(val)) {
      $err.text("전화번호는 010-0000-0000 형식이어야 합니다.").removeClass("text-green-600 hidden").addClass("text-red-600");
      phoneNumValid = false;
    } else {
      $err.text("사용가능한 전화번호입니다.").removeClass("text-red-600 hidden").addClass("text-green-600");
      phoneNumValid = true;
    }
    toggleSubmit();
  });

  // 성별, 출생연도 blur 이벤트로 유효성 검사
  $("#gen, #birthYear").on("blur", function () {
    const id = this.id;
    const val = $(this).val();
    const $err = $("#" + id + "Error");

    const isValid = id === "gen" ? validate.gen(val) : validate.birthYear(val);

    if (!isValid) {
      $err.text(id === "gen" ? "성별을 선택해주세요." : "출생 연도를 선택해주세요.").removeClass("text-green-600 hidden").addClass("text-red-600");
      if (id === "gen") genValid = false;
      if (id === "birthYear") birthYearValid = false;
    } else {
      $err.text(id === "gen" ? "성별이 선택되었습니다." : "출생 연도가 선택되었습니다.").removeClass("text-red-600 hidden").addClass("text-green-600");
      if (id === "gen") genValid = true;
      if (id === "birthYear") birthYearValid = true;
    }
    toggleSubmit();
  });

  // 폼 제출 이벤트 처리
  $("#registerForm").on("submit", function (e) {
    e.preventDefault();

    if (!userIdValid || !emailValid || !passwordValid || !confirmPasswordValid || !phoneNumValid || !genValid || !birthYearValid) {
      showToast("가입 정보를 다시 확인해주세요", "error");
      return;
    }

    const formData = new FormData(this);

    fetch("/register", {
      method: "POST",
      body: formData
    })
      .then(response => {
        if (response.ok) {
          showToast("회원가입 성공!", "success");
          setTimeout(() => window.location.href = "/login", 1000);
        } else {
          return response.json().then(data => {
            showToast(data.message || "회원가입 실패", "error");
          });
        }
      })
      .catch(error => {
        console.error(error);
        showToast("서버 오류가 발생했습니다", "error");
      });
  });
});
