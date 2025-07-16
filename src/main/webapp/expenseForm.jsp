<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<div id="deleteModal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <p>Are you sure you want to delete this entry?</p>
        <button id="confirmDelete">Yes, Delete</button>
        <button id="cancelDelete">No, Cancel</button>
    </div>
</div>

<script>

var modal = document.getElementById("deleteModal");
var btn = document.getElementById("deleteExpenseButton"); 
var span = document.getElementsByClassName("close")[0];

btn.onclick = function() {
    modal.style.display = "block";
}

span.onclick = function() {
    modal.style.display = "none";
}

document.getElementById("cancelDelete").onclick = function() {
    modal.style.display = "none";
}

document.getElementById("confirmDelete").onclick = function() {

	window.location.href = "deleteExpense?id=" + expenseID; 
}
</script>
