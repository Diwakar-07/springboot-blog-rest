class Human{

constructor(gender){
    this.gender = 'male';
}

printGender(){
    console.log(this.gender);
}

}

const h = new Human();
h.printGender();