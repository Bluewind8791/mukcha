let index = {
    init: function() {
        $("#btn-disable").on("click", () => {
            this.disable()
        });
    },

    // 회원 탈퇴 메소드
    disable: function() {
        $.ajax({
            method: 'PATCH',
            url: '/api/users',
            contentType: 'application/json;charset=utf-8'
        }).done(function() {
            alert("회원이 탈퇴되었습니다. 확인을 누르면 메인으로 돌아갑니다.");
            location.href = "/";
        }).fail(function(error) {
            alert("회원 탈퇴에 실패하였습니다.")
            alert(JSON.stringify(error));
        });
    }




}

index.init();