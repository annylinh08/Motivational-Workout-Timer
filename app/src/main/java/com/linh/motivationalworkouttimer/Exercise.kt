package com.linh.motivationalworkouttimer

class Exercise {

    var id: String? = null
    var title: String? = null
    var cycle: Int? = null
    var prepTime: Int? = null
    var workTime: Int? = null
    var restTime: Int? = null

    constructor() {}

    constructor(id: String, title: String, cycle: Int, prepTime: Int, workTime: Int, restTime: Int){
        this.id = id
        this.title = title
        this.cycle = cycle
        this.prepTime = prepTime
        this.workTime = workTime
        this.restTime = restTime
    }
}