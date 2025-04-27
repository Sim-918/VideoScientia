$(function () {
  //현재 년도
  const currentYear = new Date().getFullYear();

  // 출생 연도 선택 옵션(1900~현재)
  for (let year = currentYear; year >= 1900; year--) {
    const option = $("<option>").val(year).text(`${year}년`);
    $("#birthYear").append(option);
  }

  //출생연도 선택 후 나이를 자동 계산하여 age에 넣음
  $("#birthYear").on("change", function () {
    const year = parseInt($(this).val(), 10);
    if (!isNaN(year)) {
      $("#age").val(currentYear - year + 1);
    }
  });

  // 전화번호 입력 시 자동으로 하이폰추가
  $("#phoneNum").on("input", function () {
    let value = $(this).val().replace(/[^\d]/g, "");
    if (value.length > 3 && value.length <= 7) {
      value = value.slice(0, 3) + "-" + value.slice(3);
    } else if (value.length > 7) {
      value = value.slice(0, 3) + "-" + value.slice(3, 7) + "-" + value.slice(7, 11);
    }
    $(this).val(value);
  });

   // 각 입력값 유효성 검사 규칙
  const validate = {
    userId: v => /^[a-zA-Z0-9_]{4,20}$/.test(v),
    email: v => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v),
    password: v => /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/.test(v),
    confirmPassword: (v, p) => v === p,
    gen: v => !!v,
    birthYear: v => !!v,
    phoneNum: v => /^\d{3}-\d{4}-\d{4}$/.test(v)
  };

  // 각 입력값 통과 여부 플래그
  let userIdValid = false, emailValid = false;
  let passwordValid = false, confirmPasswordValid = false;
  let phoneNumValid = false, genValid = false, birthYearValid = false;

  // 모든 입력값이 유효할 때 제출 버튼 활성화
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

   // 아이디/이메일 fetch로 중복검사 및 유효성 검사
  function checkDuplicate(field, value, $errorEl, isFormatValid, formatMsg, existsMsg, availableMsg) {
    if (!isFormatValid(value)) {  //포맷 자체가 틀렸을 경우
      $errorEl.text(formatMsg).removeClass("text-green-600 hidden").addClass("text-red-600");
      if (field === "userId") userIdValid = false;
      if (field === "email") emailValid = false;
      toggleSubmit();
      return;
    }

    const path = field === "userId" ? "check-id" : "check-email";

    fetch(`/api/${path}?value=${encodeURIComponent(value)}`, {
      method: "GET",
      headers: {
        "Accept": "application/json"
      }
    })
    .then(response => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
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


  $("#userId").on("blur", function () {
    const val = $(this).val().trim();
    checkDuplicate("userId", val, $("#userIdError"), validate.userId, "4~20자의 영문/숫자/언더스코어만 사용 가능합니다.", "이미 존재하는 아이디입니다.", "사용가능한 아이디입니다.");
  });

  $("#email").on("blur", function () {
    const val = $(this).val().trim();
    checkDuplicate("email", val, $("#emailError"), validate.email, "올바른 이메일 주소를 입력해주세요.", "이미 존재하는 이메일입니다.", "사용가능한 이메일입니다.");
  });

  $("#password").on("blur", function () {
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

    // 비밀번호 일치여부
  $("#confirmPassword").on("blur", function () {
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

// 회원가입 fetch 요청
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
