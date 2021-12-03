const signupBtn = document.querySelector(".signup-btn");
const clientName = document.getElementById("client-name");
const clientPassword = document.getElementById("client-password");

signupBtn.addEventListener("click", () => {
    const name = clientName.value;
    const password = clientPassword.value;

    postApiClient(name, password);
})