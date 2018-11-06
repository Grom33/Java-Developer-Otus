import { Component, OnInit } from '@angular/core';
import { User } from '../shared/user';
import { Router, ActivatedRoute } from '@angular/router';
import { UserService } from '../shared/user.service';
import { FormGroup, FormBuilder, FormControl, ReactiveFormsModule } from '@angular/forms';
import { Phone } from '../shared/phone';
import { Adress } from '../shared/adress';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})

export class UserEditComponent implements OnInit {
  public user: User;
  public userForm: FormGroup;
  public nameControl: FormControl;
  public adressControl: FormControl;
  public phoneControl: FormControl;

  constructor(
    private route: ActivatedRoute, 
    private userService: UserService,
    private router: Router,
    private fb: FormBuilder) { }

  ngOnInit() {
    this.userForm = new FormGroup({
      nameControl: new FormControl(''),
      adressControl: new FormControl(''),
      phoneControl: new FormControl('')
    });

    let userId: number = parseInt(this.route.snapshot.params['userId']);
    if(Number(userId) == 0) {
        this.user = <User> {} ;
        this.user.adress = <Adress>{};
        this.user.phones = [<Phone>{}];
    }else{
      this.userService.getUserById(userId).subscribe(
       data=>{
        this.user = JSON.parse(JSON.stringify(data)) as User;
        this.userForm.controls['nameControl'].patchValue(this.user.name);
        this.userForm.controls['adressControl'].patchValue(this.user.adress.adress);
        this.userForm.controls['phoneControl'].patchValue(this.user.phones[0].number);
      });
    }
  }
  backClicked() {
    this.router.navigate(['/']);
  }
  save() {
    this.userService.save(this.prepareUser());
  }
  prepareUser() {
    this.user.name = this.userForm.controls['nameControl'].value;
    this.user.adress.adress = this.userForm.controls['adressControl'].value;
    return this.user;
  }
}
