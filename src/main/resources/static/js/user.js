let index = {
    init: function() {
        $("#btn-disable").on("click", () => {
            this.disable()
        });
        $("#btn-update").on("click", () => {
            this.update()
        });
        
    },

    // 회원 탈퇴 메소드
    disable: function() {
        $.ajax({
            method: 'PATCH',
            url: '/api/users',
            contentType: 'application/json; charset=utf-8'
        }).done(function() {
            alert("회원이 탈퇴되었습니다. 확인을 누르면 메인으로 돌아갑니다.");
            location.href = "/";
        }).fail(function(error) {
            alert("회원 탈퇴에 실패하였습니다.")
            alert(JSON.stringify(error));
        });
    },

    // 회원 정보 업데이트
    update: function() {
        let userData = {
            nickname: $("#nickname").val(),
            gender: $("#gender").val(),
            birthYear: $("#birthYear").val()
        };
        $.ajax({
            method: 'PUT',
            url: '/api/users',
            data: JSON.stringify(userData),
            contentType: 'application/json; charset=utf-8'
        }).done(function() {
            alert("회원 정보 수정이 완료되었습니다.");
            location.href = "/user/edit";
        }).fail(function(error) {
            alert("회원 정보 수정에 실패하였습니다.")
            alert(JSON.stringify(error));
        });
    }
}

index.init();