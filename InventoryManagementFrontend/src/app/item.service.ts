import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Item} from "./item";
import {environment} from "../environments/environment";

@Injectable( {
  providedIn: 'root'
})
export class ItemService {
  private apiServerUrl = environment.apiBaseUrl;
  private itemsUrl = '/items';
  private itemUrls = '/item';

  constructor(private http: HttpClient) {
  }

  // GET /items
  public getItems(): Observable<Item[]> {
    return this.http.get<Item[]>(`${this.apiServerUrl}${this.itemsUrl}`)
  }

  // GET /items/{id}
  public getItem(itemId: number): Observable<Item> {
    return this.http.get<Item>(`${this.apiServerUrl}${this.itemsUrl}/${itemId}`)
  }

  // POST /item with request body of item
  public addItem(item: Item): Observable<Item> {
    return this.http.post<Item>(`${this.apiServerUrl}${this.itemUrls}`, item)
  }

  // DELETE /items/{id}
  public deleteItem(itemId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}${this.itemsUrl}/${itemId}`)
  }

}
