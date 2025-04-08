function showModal(element) {
  const modalTitle = document.getElementById('modal-title');
  const equipmentForm = document.getElementById('equipmentForm');
  const hiddenMethod = document.getElementById('hiddenMethod');
  const addButton = document.getElementById('addButton');
  const preview = document.getElementById('preview');
  const equipmentIDField = document.getElementById('equipmentID');
  const equipmentNameField = document.getElementById('equipmentName');
  const equipmentDescriptionField = document.getElementById('equipmentDescription');
  const equipmentPriceField = document.getElementById('equipmentPrice');
  const equipmentQuantityField = document.getElementById('equipmentQuantity');

  if (element != null) {
    console.log(element);
    const equipmentID = element.getAttribute("data-id");
    const name = element.getAttribute("data-name");
    const description = element.getAttribute("data-description");
    const price = element.getAttribute("data-price");
    const quantity = element.getAttribute("data-quantity");
    const image = element.getAttribute("data-image");
    const textEdit = document.getElementById('textEdit').innerText;

    equipmentIDField.value = equipmentID;
    equipmentNameField.value = name;
    equipmentDescriptionField.value = description;
    equipmentPriceField.value = price;
    equipmentQuantityField.value = quantity;

    preview.src = image;
    preview.style.display = 'block';

    fetch(image)
        .then(res => res.blob())
        .then(blob => {
          const file = new File([blob], "existing.jpg", { type: blob.type });
          const dataTransfer = new DataTransfer();
          dataTransfer.items.add(file);
          document.getElementById('fileInput').files = dataTransfer.files;
        });

    modalTitle.innerText = textEdit;

    equipmentForm.action = "/equipment_edit";
    hiddenMethod.value = "PUT";

  } else {
    const textAdd = document.getElementById('textAdd').innerText;
    const file = document.getElementById('fileInput');

    equipmentIDField.value = "";
    equipmentNameField.value = "";
    equipmentDescriptionField.value = "";
    equipmentPriceField.value = "";
    equipmentQuantityField.value = "";
    preview.src = "";
    preview.style.display = "none";
    file.value = "";

    modalTitle.innerText = textAdd;
    equipmentForm.action = "/equipment_add";
    equipmentForm.method = "POST";
    hiddenMethod.value = "POST";
  }

  document.getElementById('modal').classList.add('active');
}

function showReservationModal(element) {
    console.log(element);

    document.getElementById('reservationModal').classList.add('active');
}

function hideModal() {
    const modal = document.getElementById('modal');
    modal.classList.remove('active');
}

function hideReservationModal() {
    const modal = document.getElementById('reservationModal');
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