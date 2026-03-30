document.addEventListener("DOMContentLoaded", async () => {
    await loadRoles();
    await loadUsers();

    document.getElementById("newUserForm").addEventListener("submit", createUser);
    document.getElementById("editUserForm").addEventListener("submit", updateUser);
    document.getElementById("deleteUserForm").addEventListener("submit", deleteUser);
});

let rolesCache = [];

async function loadUsers() {
    const response = await fetch("/api/admin/users");
    const users = await response.json();

    const tbody = document.getElementById("usersTableBody");
    tbody.innerHTML = "";

    users.forEach(user => {
        const row = `
            <tr>
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${user.roles.join(" ")}</td>
                <td>
                    <button class="btn btn-info btn-sm"
                            onclick="openEditModal(${user.id})">
                        Edit
                    </button>
                </td>
                <td>
                    <button class="btn btn-danger btn-sm"
                            onclick="openDeleteModal(${user.id})">
                        Delete
                    </button>
                </td>
            </tr>
        `;
        tbody.innerHTML += row;
    });
}

async function loadRoles() {
    const response = await fetch("/api/admin/roles");
    rolesCache = await response.json();

    fillRolesSelect("newRoles");
    fillRolesSelect("editRoles");
}

function fillRolesSelect(selectId, selectedRoleNames = []) {
    const select = document.getElementById(selectId);
    select.innerHTML = "";

    rolesCache.forEach(role => {
        const cleanRole = role.name.replace("ROLE_", "");
        const selected = selectedRoleNames.includes(cleanRole) ? "selected" : "";

        select.innerHTML += `
            <option value="${role.id}" ${selected}>${cleanRole}</option>
        `;
    });
}

async function createUser(event) {
    event.preventDefault();
    clearErrors("new");

    const user = {
        username: document.getElementById("newUsername").value,
        firstName: document.getElementById("newFirstName").value,
        lastName: document.getElementById("newLastName").value,
        age: Number(document.getElementById("newAge").value),
        email: document.getElementById("newEmail").value,
        password: document.getElementById("newPassword").value,
        roleIds: Array.from(document.getElementById("newRoles").selectedOptions)
            .map(option => Number(option.value))
    };

    const response = await fetch("/api/admin/users", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(user)
    });

    if (response.ok) {
        document.getElementById("newUserForm").reset();
        await loadUsers();

        const usersTab = new bootstrap.Tab(document.querySelector('[data-bs-target="#usersTable"]'));
        usersTab.show();
    } else {
        const errors = await response.json();
        showErrors(errors, "new");
    }
}

async function openEditModal(id) {
    clearErrors("edit");

    const response = await fetch(`/api/admin/users/${id}`);
    const user = await response.json();

    document.getElementById("editId").value = user.id;
    document.getElementById("editIdVisible").value = user.id;
    document.getElementById("editUsername").value = user.username;
    document.getElementById("editFirstName").value = user.firstName;
    document.getElementById("editLastName").value = user.lastName;
    document.getElementById("editAge").value = user.age;
    document.getElementById("editEmail").value = user.email;
    document.getElementById("editPassword").value = "";

    fillRolesSelect("editRoles", user.roles);

    const modal = new bootstrap.Modal(document.getElementById("editModal"));
    modal.show();
}

async function updateUser(event) {
    event.preventDefault();
    clearErrors("edit");

    const id = document.getElementById("editId").value;

    const user = {
        username: document.getElementById("editUsername").value,
        firstName: document.getElementById("editFirstName").value,
        lastName: document.getElementById("editLastName").value,
        age: Number(document.getElementById("editAge").value),
        email: document.getElementById("editEmail").value,
        password: document.getElementById("editPassword").value,
        roleIds: Array.from(document.getElementById("editRoles").selectedOptions)
            .map(option => Number(option.value))
    };

    const response = await fetch(`/api/admin/users/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(user)
    });

    if (response.ok) {
        bootstrap.Modal.getInstance(document.getElementById("editModal")).hide();
        await loadUsers();
    } else {
        const errors = await response.json();
        showErrors(errors, "edit");
    }
}

async function openDeleteModal(id) {
    const response = await fetch(`/api/admin/users/${id}`);
    const user = await response.json();

    document.getElementById("deleteId").value = user.id;
    document.getElementById("deleteIdVisible").value = user.id;
    document.getElementById("deleteUsername").value = user.username;
    document.getElementById("deleteFirstName").value = user.firstName;
    document.getElementById("deleteLastName").value = user.lastName;
    document.getElementById("deleteAge").value = user.age;
    document.getElementById("deleteEmail").value = user.email;
    document.getElementById("deleteRoles").value = user.roles.join(" ");

    const modal = new bootstrap.Modal(document.getElementById("deleteModal"));
    modal.show();
}

async function deleteUser(event) {
    event.preventDefault();

    const id = document.getElementById("deleteId").value;

    const response = await fetch(`/api/admin/users/${id}`, {
        method: "DELETE"
    });

    if (response.ok) {
        bootstrap.Modal.getInstance(document.getElementById("deleteModal")).hide();
        await loadUsers();
    }
}

function showErrors(errors, prefix) {
    Object.keys(errors).forEach(field => {
        const errorDiv = document.getElementById(`${prefix}${capitalize(field)}Error`);
        if (errorDiv) {
            errorDiv.textContent = errors[field];
        }
    });
}

function clearErrors(prefix) {
    document.querySelectorAll(`[id^="${prefix}"][id$="Error"]`).forEach(el => el.textContent = "");
}

function capitalize(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}