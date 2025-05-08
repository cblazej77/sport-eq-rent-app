function showModal(element) {
    const modalTitle = document.getElementById('modal-title');
    const categoryIdField = document.getElementById('categoryID');
    const categoryNameField = document.getElementById('categoryName');
    const preview = document.getElementById('preview');
    const form = document.getElementById('categoryForm');
    const hiddenMethod = document.getElementById('hiddenMethod');

    if (element != null) {
        console.log(element);
        const categoryID = element.getAttribute("data-id");
        const categoryName = element.getAttribute("data-name");
        const categoryImage = element.getAttribute("data-image");
        const catBtnEdit = document.getElementById("catBtnEdit").innerText;

        categoryIdField.value = categoryID;
        categoryNameField.value = categoryName;
        preview.src = categoryImage;
        preview.style.display = 'block';

        modalTitle.innerText = catBtnEdit;
        form.action = "/categories/edit";
        hiddenMethod.value = "PUT";
    } else {
        const catBtnAdd = document.getElementById("catBtnAdd").innerText;

        categoryIdField.value = "";
        categoryNameField.value = "";
        preview.style.display = 'none';

        modalTitle.innerText = catBtnAdd;
        form.action = "/categories/add";
        form.method = "POST";
        hiddenMethod.value = "POST";
    }

    document.getElementById('modal').classList.add('active');
}

function hideModal() {
    const modal = document.getElementById('modal');
    modal.classList.remove('active');
}

function triggerFileInput() {
    document.getElementById('fileInput').click();
}

function previewImage(event) {
    const file = event.target.files[0];
    const preview = document.getElementById('preview');

    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            preview.src = e.target.result;
            preview.style.display = 'block';
        };
        reader.readAsDataURL(file);
    }
}