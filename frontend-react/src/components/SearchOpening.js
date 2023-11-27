import { useSelector } from 'react-redux';
import { useState, useEffect } from 'react';
import { ProjectAllocationService } from '../services/api/projectAllocationService';
import { SearchService } from '../services/api/searchService';
import { Snackbar, SnackbarContent } from '@mui/material';
import { Container } from '@mui/system';
import { TextField, Button, Grid, Tooltip, Pagination } from '@mui/material';
import OpeningCard from './OpeningCard';

function SearchOpening({ loggedinUser }){
  const authToken = useSelector(state => state.auth.authToken);
  const [searchQuery, setSearchQuery] = useState('');
  const [searchedOpenings, setSearchedOpenings] = useState([]);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarColor, setSnackbarColor] = useState("red"); // Default color for errors
  const [pageSize, setPageSize] = useState(4);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await SearchService.searchProjectOpenings(authToken, pageSize, currentPage, searchQuery);
        const projectOpenings = response.openings;
        const totalElements = response.totalElements;

        const calculatedTotalPages = Math.ceil(totalElements / pageSize);
        setTotalPages(calculatedTotalPages);
        setSearchedOpenings(projectOpenings);
      } catch (error) {
        showSnackbar("Network error. Please try again later.", "red");
      }
    };

    fetchData();
  }, [searchQuery, pageSize, currentPage]);

  const applyToOpening = async (openingId, userId) => {
    try {
      // Make the API call to apply for the opening
      const response = await ProjectAllocationService.applyForOpening(openingId, userId, authToken);

      // Show success Snackbar
      showSnackbar("Successfully applied for the opening.", "green");
    } catch (error) {
      // Handle the error if the API call fails
      console.log('errorclient', error);

      if (error) {
        // Show the specific error message from the API
        showSnackbar(`${error}`, "red");
      } else {
        // If the error doesn't have a response (network error, etc.)
        showSnackbar("Network error. Please try again later.", "red");
      }
    }
  };

  const handleApplyOpening = async openingId => {
      await applyToOpening(openingId, loggedinUser.id);
  };

  const handlePageChange = (event, newPage) => {
    setCurrentPage(newPage - 1); // Subtract 1 from newPage to convert to zero-based index
  };

  const showSnackbar = (message, color) => {
    setSnackbarMessage(message);
    setSnackbarColor(color);
    setSnackbarOpen(true);
  };

  const renderOpenings = searchedOpenings.map((item) => {
    return (
      <Grid item key={item.id} xs={12} sm={6} md={4}>
        <OpeningCard opening={item} />
        <Tooltip title="Apply for the opening">
            <Button
            size='small'
            variant="outlined"
            color="primary"
            onClick={() => handleApplyOpening(item.id)}
            >
            Apply
            </Button>
        </Tooltip>
    </Grid>
    );
  });

  return(
      <Container>
          <Grid  container spacing={2} style={{ width: '100%', display: 'flex', flexDirection: 'row', justifyContent: 'center', alignContent: 'start', margin: '0 auto', backgroundColor: 'white', padding: '10px', borderRadius: '4px' }} >
              {/* Search bar */}
              <TextField
                  label="Search"
                  variant="outlined"
                  fullWidth
                  margin="normal"
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
              />
              <Grid container spacing={2}>
                {renderOpenings}
              </Grid>
              {/* Pagination */}
              <div style={{ display: 'flex', justifyContent: 'center', padding: '10px' }}>
                <Pagination
                    count={totalPages}
                    page={currentPage+1}
                    onChange={handlePageChange}
                    color="primary"
                    showFirstButton
                    showLastButton
                    shape="rounded"
                />
              </div>
              <Snackbar
              open={snackbarOpen}
              autoHideDuration={5000}
              onClose={() => setSnackbarOpen(false)}
              anchorOrigin={{
                  vertical: "top",
                  horizontal: "right",
              }}
              >
              <SnackbarContent
                  message={snackbarMessage}
                  onClose={() => setSnackbarOpen(false)}
                  sx={{ backgroundColor: snackbarColor }} // Set background color based on snackbarColor
              />
              </Snackbar>
          </Grid>
      </Container>
  );
}

export default SearchOpening;