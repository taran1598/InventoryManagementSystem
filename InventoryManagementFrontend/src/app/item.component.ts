import {Component, OnInit} from '@angular/core';
import {Item} from "./item";
import {ItemService} from "./item.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './item.html',
  styleUrls: ['./item.component.less']
})
export class ItemComponent implements OnInit{

  public items: Item[] | undefined;
  public selectedItem?: Item;


  constructor(private ItemService: ItemService) {}


  ngOnInit(): void {
    this.getItems();
  }

  public getItems(): void {
    this.ItemService.getItems().subscribe(
      (response: Item[]) => {
        this.items = response;
        let x = 1;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    )
  }

  public deleteItem(item: Item): void {
    this.ItemService.deleteItem(item.id).subscribe(
      (response) => {
        //alert(`Item ${item.id} Deleted`);
        this.ngOnInit();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }

    )
  }


}
