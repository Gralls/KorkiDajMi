package com.springer.patryk.korkidajmi.model.network.endpoints

class EndpointsName {
	class User {
		companion object {
			const val LOGIN: String = "login"
			const val CREATE_USER: String = "register"
			const val USER_DETAILS = "users/{user_id}"
		}
	}

	class Subject{
		companion object {
			const val SUBJECTS_LIST="subjects"
			const val SUBJECTS_LEVEL_LIST="subjects/levels"
		}
	}

	class Offer {
		companion object {
			const val OFFERS = "offers"
			const val OFFER_DETAILS = "offers/{offerId}"
			const val SUBMIT_OFFER = "offers/{offerId}/submit"
		}
	}

	class Submission {
		companion object {
			const val SUBMISSION_LIST = "submissions/{userId}"
			const val CHANGE_SUBMISSION_STATUS = "submissions/{submissionId}"
		}
	}
}