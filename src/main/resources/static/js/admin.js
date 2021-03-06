let index = {
    init: function() {
        $("#btn-save-company").on("click", () => {
            this.save_company();
        });
        $("#btn-save-menu").on("click", () => {
            this.save_menu();
        });
        $("#btn-update-company").on("click", () => {
            this.update_company();
        });
        $("#btn-delete-company").on("click", () => {
            this.delete_company();
        });
    },

    // 회사 추가 메소드
    save_company: function() {
        var companyName = $("#companyName").val();
        let data = {
            companyName: companyName,
            companyLogo: $("#companyLogo").val()
        }
        $.ajax({
            method: 'POST',
            url: '/api/admin/companies',
            data: JSON.stringify(data),
            contentType: 'application/json; charset=utf-8'
        }).done(function() {
            alert("회사 <"+companyName+">가 추가되었습니다.");
            location.href = "/admin";
        }).fail(function(error) {
            alert("회사 추가에 실패하였습니다. 다시 시도해주세요.");
            alert(JSON.stringify(error));
        });
    },

    // 메뉴 추가 메소드
    save_menu: function() {
        var foodName = $("#foodName").val();
        var companyId = $("#companyId").val();
        let data = {
            foodName: foodName,
            foodImage: $("#foodImage").val(),
            category: $("#category").val(),
            companyName: $("#companyName").val()
        }
        $.ajax({
            method: 'POST',
            url: '/api/admin/menus',
            data: JSON.stringify(data),
            contentType: 'application/json; charset=utf-8'
        }).done(function() {
            alert("메뉴 <"+foodName+">가 추가되었습니다.");
            location.href = "/admin/companies/" + companyId;
        }).fail(function(error) {
            alert("메뉴 추가에 실패하였습니다. 다시 시도해주세요.");
            alert(JSON.stringify(error));
        });
    },

    // 회사 수정 메소드
    update_company: function() {
        var companyId = $("#companyId").val();
        var companyName = $("#updateCompanyName").val();
        let data = {
            companyName: companyName,
            companyLogo: $("#updateCompanyLogo").val()
        }
        $.ajax({
            method: 'PUT',
            url: '/api/admin/companies/' + companyId,
            data: JSON.stringify(data),
            contentType: 'application/json; charset=utf-8'
        }).done(function() {
            alert("회사 <"+companyName+">의 정보가 수정되었습니다.");
            location.href = "/admin/companies/" + companyId;
        }).fail(function(error) {
            alert("정보 수정에 실패하였습니다. 다시 시도해주세요.");
            alert(JSON.stringify(error));
        });
    },

    // 회사 삭제 메소드
    delete_company: function() {
        var companyId = $("#companyId").val();
        var companyName = $("#companyName").val();
        $.ajax({
            method: 'DELETE',
            url: '/api/admin/companies/' + companyId,
            contentType: 'application/json; charset=utf-8'
        }).done(function() {
            alert("회사 <"+companyName+">이 삭제되었습니다.");
            location.href = "/admin";
        }).fail(function(error) {
            alert("삭제에 실패하였습니다. 다시 시도해주세요.");
            alert(JSON.stringify(error));
        });
    }

}

index.init();

// 메뉴 수정 메소드
function update_menu(index) {
    var companyId = $("#companyId").val();
    var foodId = $("#updateFoodId"+index).val();
    var updateName = $("#updateFoodName"+index).val();
    let data = {
        foodName: updateName,
        foodImage: $("#updateFoodImage"+index).val(),
        category: $("#updateCategory"+index).val()
    }
    console.log("index = "+index);
    console.log("foodId = "+foodId);
    console.log("FoodName = "+updateName);
    console.log("companyId = "+companyId);
    console.log("foodImage = "+data.foodImage);
    console.log("category = "+data.category);
    $.ajax({
        method: 'PUT',
        url: '/api/admin/menus/' + foodId,
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8'
    }).done(function() {
        alert("메뉴 <"+updateName+">가 수정되었습니다.");
        location.href = "/admin/companies/" + companyId;
    }).fail(function(error) {
        alert("메뉴 수정에 실패하였습니다. 다시 시도해주세요.");
        alert(JSON.stringify(error));
    });
}

// 메뉴 삭제 메소드
function delete_menu(index) {
    var companyId = $("#companyId").val();
    var foodId = $("#updateFoodId"+index).val();
    var foodName = $("#updateFoodName"+index).val();
    $.ajax({
        method: 'DELETE',
        url: '/api/admin/menus/' + foodId,
        contentType: 'application/json; charset=utf-8',
    }).done(function() {
        alert("메뉴 <"+foodName+">이 삭제되었습니다.");
        location.href = "/admin/companies/" + companyId;
    })
    .fail(function(error) {
        alert("삭제에 실패하였습니다. 다시 시도해주세요.");
        alert(JSON.stringify(error));
    });
}