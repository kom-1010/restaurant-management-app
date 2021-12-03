const addCategoryBtn = document.querySelector(".add-category-btn");
const categoryInput = document.getElementById("category");

addCategoryBtn.addEventListener("click", () => {
    const name = categoryInput.value;
    postApiCategory(name);
})