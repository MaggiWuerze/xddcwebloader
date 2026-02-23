type CrudRepository<T, F> = {
  get: (id: string) => Promise<T>;
  create: (data: Omit<F, 'id'>) => Promise<T>;
  update: (id: string, data: Partial<Omit<F, 'id'>>) => Promise<T>;
  validate: (entity: Partial<T> | Partial<F>) => { issues: { message: string; path: string[] }[] };
};
