const form = document.querySelectorAll("form");


function handleSubmit(event) {
    alert("Polecenie przyjęte");
    console.log("Alert potwierdzający wyświetlony");
}


form .forEach(function (form) {
    form.addEventListener("submit", handleSubmit, {once: true});
})
