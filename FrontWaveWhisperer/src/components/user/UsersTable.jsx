import React, { useState, useEffect } from "react"

const UsersTable = ({ userInfo, handleDelete }) => {
	const [filteredUsers, setFilteredUsers] = useState(userInfo)

	return (
		<section className="p-4">
			<table className="table table-bordered table-hover shadow">
				<thead>
					<tr>
						<th>ID</th>
                        <th>Email</th>
                        <th>FirstName</th>
                        <th>LastName</th>
						<th colSpan={1}>Actions</th>
					</tr>
				</thead>
				<tbody className="text-center">
					{filteredUsers.map((user) => (
						<tr key={user.id}>
							<td>{user.id}</td>
							<td>{user.email}</td>
              <td>{user.firstName}</td>
              <td>{user.lastName}</td>
							<td>
								<button
									className="btn btn-danger btn-sm"
									onClick={() => handleDelete(user.email)}>
									Delete
								</button>
							</td>
						</tr>
					))}
				</tbody>
			</table>
		</section>
	)
}

export default UsersTable