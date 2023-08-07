$(document).ready(function() {

    $("#submit-btn").click(function (event){
        let form = $("#story-create-form")[0];

        if(!form.checkValidity()){
            event.preventDefault();
            event.stopPropagation();
            form.classList.add('was-validated');
        }
    })
});