let index = {
    init: function() {
        $("#btn-save-company").on("click", () => {
            this.save_company();
        });
        $("#btn-save-menu").on("click", () => {
            this.save_menu();
        });
        $("#btn-update-menu").on("click", () => {
            this.update_menu();
        });
        $("#btn-update-company").on("click", () => {
            this.update_company();
        });
        $("#btn-delete-company").on("click", () => {
            this.delete_company();
        });
        $("#btn-delete-menu").on("click", () => {
            this.delete_menu();
        });
    },

    // 메뉴 삭제 메소드
    delete_menu: function() {
        var companyId = $("#companyId").val();
        var foodId = $("#updateFoodId").val();
        var foodName = $("#updateFoodName").val();
        $.ajax({
            method: 'DELETE',
            url: '/api/admin/menus/' + foodId,
            contentType: 'application/json; charset=utf-8',
        }).done(function() {
            alert("메뉴 <"+foodName+">이 삭제되었습니다.");
            location.href = "/admin/companies/" + companyId;
        })
        .fail(function(error) {
            // readyState 4, responseText, status 400, statusText: error
            alert("삭제에 실패하였습니다. 다시 시도해주세요.");
            alert(JSON.stringify(error));
        })
        ;
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

    // 메뉴 수정 메소드
    update_menu: function() {
        var foodId = $("#updateFoodId").val();
        var foodName = $("#updateFoodName").val();
        var companyId = $("#companyId").val();
        let data = {
            foodName: foodName,
            foodImage: $("#updateFoodImage").val(),
            category: $("#updateCategory").val(),
            companyName: $("#updateCompanyName").val()
        }
        $.ajax({
            method: 'PUT',
            url: '/api/admin/menus/' + foodId,
            data: JSON.stringify(data),
            contentType: 'application/json; charset=utf-8'
        }).done(function() {
            alert("메뉴 <"+foodName+">가 수정되었습니다.");
            location.href = "/admin/companies/" + companyId;
        }).fail(function(error) {
            alert("메뉴 수정에 실패하였습니다. 다시 시도해주세요.");
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