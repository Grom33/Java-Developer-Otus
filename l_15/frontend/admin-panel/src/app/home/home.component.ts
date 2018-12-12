import { Component, OnInit } from '@angular/core';
import { UserService } from '../shared/user.service';
import { User } from '../shared/user'

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  users: User[] = [];

  constructor(private userService: UserService) { }

  ngOnInit() {
   this.userService.getUsers().subscribe(
    data => {
      console.log("in home");
      this.users = JSON.parse(JSON.stringify(data)) as User[];
      console.log(this.users);
    });

  
  }

}
