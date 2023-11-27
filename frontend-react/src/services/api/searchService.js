// api/searchService.js

import axios from 'axios';

const SEARCH_SERVICE_BASE_URL = 'http://localhost:9093'; 

export class SearchService {
  static searchProjectOpenings = async (authToken, pageSize, pageNumber, searchText) => {
      try {
        const url = `${SEARCH_SERVICE_BASE_URL}/api/v1/search?searchText=${searchText}&pageSize=${pageSize}&pageNumber=${pageNumber}`;
        const response = await axios.get(url, { headers: { "Authorization": `Bearer ${authToken}` } });
        return response.data;
      } catch (error) {
        throw new Error('Unable to search project openings');
      }
    };      
}
