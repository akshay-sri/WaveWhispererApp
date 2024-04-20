import React, { useEffect, useState } from 'react'
import { deleteUser, getAllUsers } from '../utils/ApiFunctions'
import UsersTable from './UsersTable'
import Header from '../common/Header'

const ExistingUsers = () => {
    const [userInfo, setUserInfo] = useState([])
	const [isLoading, setIsLoading] = useState(true)
	const [error, setError] = useState("")

	useEffect(() => {
		setTimeout(() => {
			getAllUsers()
				.then((data) => {
					setUserInfo(data)
					setIsLoading(false)
				})
				.catch((error) => {
					setError(error.message)
					setIsLoading(false)
				})
		}, 1000)
	}, [])
    const handleDelete = async (userId) => {
		const confirmed = window.confirm(
			"Are you sure you want to delete this account? This action cannot be undone."
		)
		if (confirmed) {
			await deleteUser(userId)
				.then((response) => {
					setMessage(response.data)
					localStorage.removeItem("token")
					localStorage.removeItem("userId")
					localStorage.removeItem("userRole")

					navigate("/")
					
				})
				.catch((error) => {
					setErrorMessage(error.data)
				})
		}
	}
  return (
    <section style={{ backgroundColor: "whitesmoke" }}>
    <Header title={"Existing Users"} />
    {error && <div className="text-danger">{error}</div>}
    {isLoading ? (
        <div>Loading existing users</div>
    ) : (
        <UsersTable
            userInfo={userInfo}
            handleDelete={handleDelete}
        />
    )}
</section>
  )
}

export default ExistingUsers