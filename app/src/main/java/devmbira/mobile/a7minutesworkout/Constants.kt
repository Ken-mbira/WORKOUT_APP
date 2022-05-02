package devmbira.mobile.a7minutesworkout

object Constants {
    fun defaultExerciseList():ArrayList<ExerciseModel>{
        val exerciseList = ArrayList<ExerciseModel>()
        val burpees = ExerciseModel(
            1,
            "Burpees",
            R.drawable.burpees_exercise,
            isCompleted = false,
            isSelected = false
        )
        exerciseList.add(burpees)

        val plank = ExerciseModel(
            2,
            "Plank",
            R.drawable.plank_exercise,
            isCompleted = false,
            isSelected = false
        )
        exerciseList.add(plank)

        val pushup = ExerciseModel(
            3,
            "Push Up",
            R.drawable.pushup_exercise,
            isCompleted = false,
            isSelected = false
        )
        exerciseList.add(pushup)

        val sidewaysPlank = ExerciseModel(
            4,
            "Sideways Plank",
            R.drawable.plank_exercise,
            isCompleted = false,
            isSelected = false
        )
        exerciseList.add(sidewaysPlank)

        val sitUp = ExerciseModel(
            5,
            "Sit Up",
            R.drawable.sit_up_exercise,
            isCompleted = false,
            isSelected = false
        )
        exerciseList.add(sitUp)
        return exerciseList
    }
}