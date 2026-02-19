import * as React from 'react';
import type {
  GridFilterModel,
  GridPaginationModel,
  GridSortModel,
} from '@mui/x-data-grid';
import { useLocation, useNavigate, useSearchParams } from 'react-router';

const DEFAULT_PAGE_SIZE = 10;

export function useUrlDataGridState(initialPageSize = DEFAULT_PAGE_SIZE) {
  const { pathname } = useLocation();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const [paginationModel, setPaginationModel] = React.useState<GridPaginationModel>({
    page: searchParams.get('page') ? Number(searchParams.get('page')) : 0,
    pageSize: searchParams.get('pageSize') ? Number(searchParams.get('pageSize')) : initialPageSize,
  });

  const [filterModel, setFilterModel] = React.useState<GridFilterModel>(
    searchParams.get('filter')
      ? JSON.parse(searchParams.get('filter') ?? '')
      : { items: [] },
  );

  const [sortModel, setSortModel] = React.useState<GridSortModel>(
    searchParams.get('sort') ? JSON.parse(searchParams.get('sort') ?? '') : [],
  );

  const updateUrl = React.useCallback(() => {
    const qs = searchParams.toString();
    navigate(`${pathname}${qs ? '?' : ''}${qs}`);
  }, [navigate, pathname, searchParams]);

  const onPaginationModelChange = React.useCallback((model: GridPaginationModel) => {
    setPaginationModel(model);
    searchParams.set('page', String(model.page));
    searchParams.set('pageSize', String(model.pageSize));
    updateUrl();
  }, [searchParams, updateUrl]);

  const onFilterModelChange = React.useCallback((model: GridFilterModel) => {
    setFilterModel(model);

    if (model.items.length > 0 || (model.quickFilterValues?.length ?? 0) > 0) {
      searchParams.set('filter', JSON.stringify(model));
    } else {
      searchParams.delete('filter');
    }
    updateUrl();
  }, [searchParams, updateUrl]);

  const onSortModelChange = React.useCallback((model: GridSortModel) => {
    setSortModel(model);

    if (model.length > 0) searchParams.set('sort', JSON.stringify(model));
    else searchParams.delete('sort');

    updateUrl();
  }, [searchParams, updateUrl]);

  return {
    paginationModel,
    sortModel,
    filterModel,
    onPaginationModelChange,
    onFilterModelChange,
    onSortModelChange,
  };
}
