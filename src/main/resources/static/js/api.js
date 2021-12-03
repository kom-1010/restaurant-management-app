function postApiClient(name, password){
    const item = {
        "name": name,
        "password": password,
    };
    const url = "http://localhost:8080/api/v1/client"
    const options = {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(item),
    };

    fetch(url, options)
    .then(response => {
        alert("회원가입 성공");
        location.href = "/";
    })
    .catch(e => {
        alert("회원가입 실패")
        console.log(e)
    })
}

function postApiCategory(name) {
    const url = "http://localhost:8080/api/v1/categories?name=" + name;
    const options = {
        method: "POST",
    };

    fetch(url, options)
        .then(response => {
            alert("카테고리 등록 성공");
            location.href = "/";
        })
        .catch(e => {
            alert("카테고리 등록 실패")
            console.log(e)
        });
}

function postApiFood(name, price, size, category, type) {
    const url = "http://localhost:8080/api/v1/foods";

    const item = {
        "name": name,
        "type": type,
        "categoryName": category,
        "price": price,
        "size": size
    };

    const options = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(item)
    };

    fetch(url, options)
        .then(response => {
            alert("메뉴 등록 성공");
            location.href = "/";
        })
        .catch(e => {
            alert("메뉴 등록 실패")
            console.log(e)
        });
}

function postApiOrder(name, password, foodCounts) {
    const url = "http://localhost:8080/api/v1/orders";

    const item = {
        "clientName": name,
        "password": password,
        "foodCounts": foodCounts
    };

    const options = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(item)
    };

    fetch(url, options)
        .then(response => {
            alert("주문 등록 성공");
            location.href = "/list";
        })
        .catch(e => {
            alert("주문 등록 실패")
            console.log(e)
        });
}

function putApiOrder(id){
    const url = "http://localhost:8080/api/v1/orders/" + id;
    const options = {
        method: "PUT"
    };
    fetch(url, options)
            .then(response => {
                alert("주문 수정 성공");
                location.href = "/list";
            })
            .catch(e => {
                alert("주문 수정 실패")
                console.log(e)
            });
}