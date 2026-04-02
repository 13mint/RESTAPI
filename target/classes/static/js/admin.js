async function loadUsers() {
    try {
        const response = await fetch('/api/admin/users');

        if (!response.ok) {
            throw new Error('Failed to load users');
        }

        const users = await response.json();
        console.log("Users from API:", users);

        const tableBody = document.getElementById('usersTableBody');
        if (!tableBody) {
            console.error("usersTableBody not found");
            return;
        }

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
        console.log("Roles from API:", roles);

        const newRolesSelect = document.getElementById('newRoles');
        const editRolesSelect = document.getElementById('editRoles');

        if (newRolesSelect) newRolesSelect.innerHTML = '';
        if (editRolesSelect) editRolesSelect.innerHTML = '';

        roles.forEach(role => {
            const roleName = role.name.replace('ROLE_', '');

            if (newRolesSelect) {
                const option1 = document.createElement('option');
                option1.value = role.id;
                option1.textContent = roleName;
                newRolesSelect.appendChild(option1);
            }

            if (editRolesSelect) {
                const option2 = document.createElement('option');
                option2.value = role.id;
                option2.textContent = roleName;
                editRolesSelect.appendChild(option2);
            }
        });

    } catch (error) {
        console.error('Error loading roles:', error);
    }
}

function fillEditModal(button) {
    document.getElementById('editIdVisible').value = button.getAttribute('data-id') || '';
    document.getElementById('editId').value = button.getAttribute('data-id') || '';
    document.getElementById('editUsername').value = button.getAttribute('data-username') || '';
    document.getElementById('editFirstName').value = button.getAttribute('data-firstname') || '';
    document.getElementById('editLastName').value = button.getAttribute('data-lastname') || '';
    document.getElementById('editAge').value = button.getAttribute('data-age') || '';
    document.getElementById('editEmail').value = button.getAttribute('data-email') || '';

    const rolesString = button.getAttribute('data-roles');
    const selectedRoles = rolesString ? rolesString.split(',') : [];

    const editRoles = document.getElementById('editRoles');
    if (editRoles) {
        Array.from(editRoles.options).forEach(option => {
            option.selected = selectedRoles.includes(option.textContent);
        });
    }
}

function fillDeleteModal(button) {
    const deleteId = document.getElementById('deleteId');
    const deleteIdVisible = document.getElementById('deleteIdVisible');
    const deleteUsername = document.getElementById('deleteUsername');
    const deleteFirstName = document.getElementById('deleteFirstName');
    const deleteLastName = document.getElementById('deleteLastName');
    const deleteAge = document.getElementById('deleteAge');
    const deleteEmail = document.getElementById('deleteEmail');
    const deleteRoles = document.getElementById('deleteRoles');

    if (deleteId) deleteId.value = button.getAttribute('data-id') || '';
    if (deleteIdVisible) deleteIdVisible.value = button.getAttribute('data-id') || '';
    if (deleteUsername) deleteUsername.value = button.getAttribute('data-username') || '';
    if (deleteFirstName) deleteFirstName.value = button.getAttribute('data-firstname') || '';
    if (deleteLastName) deleteLastName.value = button.getAttribute('data-lastname') || '';
    if (deleteAge) deleteAge.value = button.getAttribute('data-age') || '';
    if (deleteEmail) deleteEmail.value = button.getAttribute('data-email') || '';
    if (deleteRoles) deleteRoles.value = button.getAttribute('data-roles') || '';
}