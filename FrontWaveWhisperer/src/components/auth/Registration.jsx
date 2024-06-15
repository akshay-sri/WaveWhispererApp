import React, { useState } from "react"
import { registerUser } from "../utils/ApiFunctions"
import { Link } from "react-router-dom"

const Registration = () => {
	const [registration, setRegistration] = useState({
		photo: null,
		firstName: "",
		lastName: "",
		email: "",
		password: ""
	})

	const [errorMessage, setErrorMessage] = useState("")
	const [successMessage, setSuccessMessage] = useState("")
	const [imagePreview, setImagePreview] = useState("")

	const handleInputChange = (e) => {
		setRegistration({ ...registration, [e.target.name]: e.target.value })
	}

	const handleImageChange = (e) => {
		const selectedImage = e.target.files[0]
		setRegistration({ ...registration, photo: selectedImage })
		setImagePreview(URL.createObjectURL(selectedImage))
	}

	const handleRegistration = async (e) => {
		e.preventDefault()
		try {
			const result = await registerUser(registration.photo, registration.firstName, 
				registration.lastName, registration.email, registration.password)
			setSuccessMessage(result)
			setErrorMessage("")
			setRegistration({photo: null, firstName: "", lastName: "", email: "", password: "" })
			setImagePreview("")
		} catch (error) {
			setSuccessMessage("")
			setErrorMessage(`Registration error : ${error.message}`)
		}
		setTimeout(() => {
			setErrorMessage("")
			setSuccessMessage("")
		}, 5000)
	}

	return (
		<section className="container col-6 mt-5 mb-5">
			{errorMessage && <p className="alert alert-danger">{errorMessage}</p>}
			{successMessage && <p className="alert alert-success">{successMessage}</p>}

			<h2>Register</h2>
			<form onSubmit={handleRegistration}>
				<div className="mb-3 row">
					<label htmlFor="firstName" className="col-sm-2 col-form-label">
						First Name
					</label>
					<div className="col-sm-10">
						<input
							id="firstName"
							required
							name="firstName"
							type="text"
							className="form-control"
							value={registration.firstName}
							onChange={handleInputChange}
						/>
					</div>
				</div>

				<div className="mb-3 row">
					<label htmlFor="lastName" className="col-sm-2 col-form-label">
						Last Name
					</label>
					<div className="col-sm-10">
						<input
							id="lastName"
							required
							name="lastName"
							type="text"
							className="form-control"
							value={registration.lastName}
							onChange={handleInputChange}
						/>
					</div>
				</div>

				<div className="mb-3 row">
					<label htmlFor="email" className="col-sm-2 col-form-label">
						Email
					</label>
					<div className="col-sm-10">
						<input
							id="email"
							required
							name="email"
							type="email"
							className="form-control"
							value={registration.email}
							onChange={handleInputChange}
						/>
					</div>
				</div>

				<div className="mb-3 row">
					<label htmlFor="password" className="col-sm-2 col-form-label">
						Password
					</label>
					<div className="col-sm-10">
						<input
							type="password"
							className="form-control"
							id="password"
							required
							name="password"
							value={registration.password}
							onChange={handleInputChange}
						/>
					</div>
				</div>
				<div className="mb-3">
								<label htmlFor="photo" className="form-label">
								 Photo
								</label>
								<input
									required
									name="photo"
									id="photo"
									type="file"
									className="form-control"
									onChange={handleImageChange}
								/>
								{imagePreview && (
									<img
										src={imagePreview}
										alt="Preview user photo"
										style={{ maxWidth: "400px", maxHeight: "400px" }}
										className="mb-3"></img>
								)}
							</div>
				<div className="mb-3">
					<button type="submit" className="btn btn-hotel" style={{ marginRight: "10px" }}>
						Register
					</button>
					<span style={{ marginLeft: "10px" }}>
						Already have an account? <Link to={"/login"}>Login</Link>
					</span>
				</div>
			</form>
		</section>
	)
}

export default Registration