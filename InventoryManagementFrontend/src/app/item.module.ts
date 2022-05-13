import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { ItemComponent } from './item.component';
import {ItemService} from "./item.service";
import {HttpClientModule} from "@angular/common/http";

@NgModule({
  declarations: [
    ItemComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [ItemService],
  bootstrap: [ItemComponent]
})
export class ItemModule { }
