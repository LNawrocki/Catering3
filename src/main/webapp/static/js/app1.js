document.addEventListener('DOMContentLoaded', function () {

    const loadingIcon = document.getElementById('loadingIcon');
    const buttonList = document.querySelectorAll("#loadingButton");

    buttonList.forEach(function (button) {
        button.addEventListener('click', function () {
            document.body.classList.add('loading');
            loadingIcon.style.display = 'block';

        })});


    const linkList = document.querySelectorAll("a");

    linkList.forEach(function (link) {
        link.addEventListener('click', function () {
            document.body.classList.add('loading');
            loadingIcon.style.display = 'block';

        })});

    });