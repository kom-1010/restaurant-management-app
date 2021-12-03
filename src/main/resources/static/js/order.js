const addOrderBtn = document.querySelector(".add-order-btn");
const clientNameInput = document.getElementById("client-name");
const clientPasswordInput = document.getElementById("client-password");
const foodInfo = document.querySelectorAll(".food-info");
const foodCheckBox = document.querySelectorAll(".food-checkbox");
const foodId = document.querySelectorAll(".food-id");
const foodName = document.querySelectorAll(".food-name");

addOrderBtn.addEventListener("click", () => {
    const name = clientNameInput.value;
    const password = clientPasswordInput.value;

    let foodCounts = [];
    for(let i=0;i<foodInfo.length;i++){
        if(foodCheckBox[i].checked){
            foodCounts.push({
                "foodId": foodId[i].innerHTML,
                "count": 2,
                "foodName": foodName[i].innerHTML
            });
        }
    }
    postApiOrder(name, password, foodCounts);
});