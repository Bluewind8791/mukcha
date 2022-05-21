let index = {
    init: function() {
        $("#btn-delete-review").on("click", () => {
            this.delete_review()
        });
        $("#btn-eaten").on("click", () => {
            this.update_eaten()
        });
        $("#btn-save").on("click", () => {
            this.save_review()
        });
    },

    // 리뷰 삭제 메소드
    delete_review: function() {
        let foodId = $("#foodId").val();
        $.ajax({
            method: 'DELETE',
            url: '/api/reviews/' + foodId,
            contentType: 'application/json; charset=utf-8'
        }).done(function() {
            alert("리뷰가 삭제되었습니다.");
            location.href = "/menu/" + foodId;
        }).fail(function(error) {
            alert("리뷰 삭제가 실패하였습니다. 다시 시도해주세요.");
            alert(JSON.stringify(error));
        });
    },

    // 먹은 날짜 추가/수정
    update_eaten: function() {
        let foodId = $("#foodId").val();
        let data = {
            eatenDate: $("#datepicker").val()
        };
        $.ajax({
            method: 'POST',
            url: '/api/reviews/eaten/' + foodId,
            data: JSON.stringify(data),
            contentType: 'application/json; charset=utf-8'
        }).done(function() {
            alert("먹은 날짜가 적용되었습니다.");
            location.href = "/menu/" + foodId;
        }).fail(function(error) {
            alert("실패하였습니다. 다시 시도해주세요.")
            alert(JSON.stringify(error));
        });
    },

    // 리뷰 저장/수정
    save_review: function() {
        let foodId = $("#foodId").val();
        const rateList = document.getElementsByName("rating");
        var rate;
        rateList.forEach((node) => {
            if (node.checked) {
                rate = node.value;
            }
        })
        let data = {
            comment: $("#comment").val(),
            rating : rate
        };
        $.ajax({
            method: 'POST',
            url: '/api/reviews/' + foodId,
            data: JSON.stringify(data),
            contentType: 'application/json; charset=utf-8'
        }).done(function() {
            location.href = "/menu/" + foodId;
        }).fail(function(error) {
            alert("리뷰에 실패하였습니다. 다시 시도해주세요.")
            alert(JSON.stringify(error));
        });
    }
}

index.init();