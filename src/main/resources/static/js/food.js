const addFoodBtn = document.querySelector(".add-food-btn");
const nameInput = document.getElementById("food-name");
const priceInput = document.getElementById("food-price");
const sizeInput = document.getElementById("food-size");
const categoryInput = document.getElementById("food-category");
const typeInput = document.getElementById("food-type");

addFoodBtn.addEventListener("click", () => {
    const name = nameInput.value;
    const price = priceInput.value;
    const size = sizeInput.value;
    const category = categoryInput.value;
    let type;
    if(typeInput.value === "식사"){
        type = "M";
    } else {
        type = "D";
    }

    postApiFood(name, price, size, category, type);
})