import { Component, OnInit } from '@angular/core';
import { User } from '../shared/user';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../shared/user.service';

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit {
  user: User;

  constructor(private route: ActivatedRoute, private userService: UserService) { }

  ngOnInit() {
    let userId: number = parseInt(this.route.snapshot.params['userId']);
    this.userService.getUserById(userId).subscribe(
       data=>{
        this.user = JSON.parse(JSON.stringify(data)) as User;
      });
  }
  remove(){
    this.userService.remove(this.user.id);
  }
}