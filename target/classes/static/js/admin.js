document.addEventListener("DOMContentLoaded", () => {
    loadUsers();
    loadRoles();

    document.getElementById("newUserForm").addEventListener("submit", createUser);
    document.getElementById("editUserForm").addEventListener("submit", updateUser);
    document.getElementById("deleteUserForm").addEventListener("submit", deleteUser);
});

async function loadUsers() {
    try {
        const response = await fetch('/api/admin/users');

        if (!response.ok) {
            throw new Error('Failed to load users');
        }

        const users = await response.json();
        const tableBody = document.getElementById('usersTableBody');

        tableBody.innerHTML = '';

        users.forEach(user => {
            const row = document.createElement('tr');

            row.innerHTML = `
                <td>${user.id ?? ''}</td>
                <td>${user.username ?? ''}</td>
                <td>${user.firstName ?? ''}</td>
                <td>${user.lastName ?? ''}</td>
                <td>${user.age ?? ''}</td>
                <td>${user.email ?? ''}</td>
                <td>${user.roles ? user.roles.join(' ') : ''}</td>
                <td>
                    <button class="btn btn-info btn-sm"
                            data-bs-toggle="modal"
                            data-bs-target="#editModal"
                            data-id="${user.id ?? ''}"
                            data-username="${user.username ?? ''}"
                            data-firstname="${user.firstName ?? ''}"
                            data-lastname="${user.lastName ?? ''}"
                            data-age="${user.age ?? ''}"
                            data-email="${user.email ?? ''}"
                            data-roles="${user.roles ? user.roles.join(',') : ''}"
                            onclick="fillEditModal(this)">
                        Edit
                    </button>
                </td>
                <td>
                    <button class="btn btn-danger btn-sm"
                            data-bs-toggle="modal"
                            data-bs-target="#deleteModal"
                            data-id="${user.id ?? ''}"
                            data-username="${user.username ?? ''}"
                            data-firstname="${user.firstName ?? ''}"
                            data-lastname="${user.lastName ?? ''}"
                            data-age="${user.age ?? ''}"
                            data-email="${user.email ?? ''}"
                            data-roles="${user.roles ? user.roles.join(' ') : ''}"
                            onclick="fillDeleteModal(this)">
                        Delete
                    </button>
                </td>
            `;

            tableBody.appendChild(row);
        });

    } catch (error) {
        console.error('Error loading users:', error);
    }
}

async function loadRoles() {
    try {
        const response = await fetch('/api/admin/roles');

        if (!response.ok) {
            throw new Error('Failed to load roles');
        }

        const roles = await response.json();

        const newRolesSelect = document.getElementById('newRoles');
        const editRolesSelect = document.getElementById('editRoles');

        newRolesSelect.innerHTML = '';
        editRolesSelect.innerHTML = '';

        roles.forEach(role => {
            const roleName = role.name.replace('ROLE_', '');

            const option1 = document.createElement('option');
            option1.value = role.id;
            option1.textContent = roleName;
            newRolesSelect.appendChild(option1);

            const option2 = document.createElement('option');
            option2.value = role.id;
            option2.textContent = roleName;
            editRolesSelect.appendChild(option2);
        });

    } catch (error) {
        console.error('Error loading roles:', error);
    }
}

function getSelectedRoles(selectId) {
    return Array.from(document.getElementById(selectId).selectedOptions)
        .map(option => Number(option.value));
}

function clearErrors(prefix) {
    document.getElementById(`${prefix}UsernameError`).textContent = '';
    document.getElementById(`${prefix}FirstNameError`).textContent = '';
    document.getElementById(`${prefix}LastNameError`).textContent = '';
    document.getElementById(`${prefix}AgeError`).textContent = '';
    document.getElementById(`${prefix}EmailError`).textContent = '';
    document.getElementById(`${prefix}PasswordError`).textContent = '';
    document.getElementById(`${prefix}RolesError`).textContent = '';
}

function showErrors(errors, prefix) {
    clearErrors(prefix);

    if (errors.username) document.getElementById(`${prefix}UsernameError`).textContent = errors.username;
    if (errors.firstName) document.getElementById(`${prefix}FirstNameError`).textContent = errors.firstName;
    if (errors.lastName) document.getElementById(`${prefix}LastNameError`).textContent = errors.lastName;
    if (errors.age) document.getElementById(`${prefix}AgeError`).textContent = errors.age;
    if (errors.email) document.getElementById(`${prefix}EmailError`).textContent = errors.email;
    if (errors.password) document.getElementById(`${prefix}PasswordError`).textContent = errors.password;
    if (errors.roleIds) document.getElementById(`${prefix}RolesError`).textContent = errors.roleIds;
}

async function createUser(event) {
    event.preventDefault();
    clearErrors("new");

    const user = {
        username: document.getElementById("newUsername").value.trim(),
        firstName: document.getElementById("newFirstName").value.trim(),
        lastName: document.getElementById("newLastName").value.trim(),
        age: document.getElementById("newAge").value
            ? Number(document.getElementById("newAge").value)
            : null,
        email: document.getElementById("newEmail").value.trim(),
        password: document.getElementById("newPassword").value.trim(),
        roleIds: getSelectedRoles("newRoles")
    };

    console.log("Creating user:", user);

    try {
        const response = await fetch("/api/admin/users", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        });

        console.log("Create response status:", response.status);

        if (response.ok) {
            document.getElementById("newUserForm").reset();
            clearErrors("new");
            await loadUsers();

            const usersTab = new bootstrap.Tab(
                document.querySelector('[data-bs-target="#usersTable"]')
            );
            usersTab.show();
        } else {
            const errors = await response.json();
            console.log("Create errors:", errors);
            showErrors(errors, "new");
        }
    } catch (error) {
        console.error("Create user error:", error);
    }
}

function fillEditModal(button) {
    clearErrors("edit");

    document.getElementById('editIdVisible').value = button.getAttribute('data-id') || '';
    document.getElementById('editId').value = button.getAttribute('data-id') || '';
    document.getElementById('editUsername').value = button.getAttribute('data-username') || '';
    document.getElementById('editFirstName').value = button.getAttribute('data-firstname') || '';
    document.getElementById('editLastName').value = button.getAttribute('data-lastname') || '';
    document.getElementById('editAge').value = button.getAttribute('data-age') || '';
    document.getElementById('editEmail').value = button.getAttribute('data-email') || '';
    document.getElementById('editPassword').value = '';

    const rolesString = button.getAttribute('data-roles');
    const selectedRoles = rolesString ? rolesString.split(',') : [];

    const editRoles = document.getElementById('editRoles');
    Array.from(editRoles.options).forEach(option => {
        option.selected = selectedRoles.includes(option.textContent);
    });
}

async function updateUser(event) {
    event.preventDefault();
    clearErrors("edit");

    const id = document.getElementById("editId").value;

    const user = {
        id: id,
        username: document.getElementById("editUsername").value.trim(),
        firstName: document.getElementById("editFirstName").value.trim(),
        lastName: document.getElementById("editLastName").value.trim(),
        age: document.getElementById("editAge").value
            ? Number(document.getElementById("editAge").value)
            : null,
        email: document.getElementById("editEmail").value.trim(),
        password: document.getElementById("editPassword").value.trim(),
        roleIds: getSelectedRoles("editRoles")
    };

    console.log("Updating user:", user);

    try {
        const response = await fetch(`/api/admin/users/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        });

        console.log("Update response status:", response.status);

        if (response.ok) {
            const modal = bootstrap.Modal.getInstance(document.getElementById("editModal"));
            modal.hide();
            await loadUsers();
        } else {
            const errors = await response.json();
            console.log("Update errors:", errors);
            showErrors(errors, "edit");
        }
    } catch (error) {
        console.error("Update user error:", error);
    }
}

function fillDeleteModal(button) {
    document.getElementById('deleteId').value = button.getAttribute('data-id') || '';
    document.getElementById('deleteIdVisible').value = button.getAttribute('data-id') || '';
    document.getElementById('deleteUsername').value = button.getAttribute('data-username') || '';
    document.getElementById('deleteFirstName').value = button.getAttribute('data-firstname') || '';
    document.getElementById('deleteLastName').value = button.getAttribute('data-lastname') || '';
    document.getElementById('deleteAge').value = button.getAttribute('data-age') || '';
    document.getElementById('deleteEmail').value = button.getAttribute('data-email') || '';
    document.getElementById('deleteRoles').value = button.getAttribute('data-roles') || '';
}

async function deleteUser(event) {
    event.preventDefault();

    const id = document.getElementById("deleteId").value;

    const response = await fetch(`/api/admin/delete?id=${id}`, {
        method: "DELETE"
    });

    if (response.ok) {
        const modal = bootstrap.Modal.getInstance(document.getElementById("deleteModal"));
        modal.hide();
        loadUsers();
    } else {
        alert("Failed to delete user");
    }
}