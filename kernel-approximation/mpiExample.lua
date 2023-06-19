require 'mpiT'

mpiT.Init()

_rank = torch.IntStorage(1)
mpiT.Comm_rank(mpiT.COMM_WORLD,_rank)
rank = _rank[1]

_size = torch.IntStorage(1)
mpiT.Comm_size(mpiT.COMM_WORLD,_size)
size = _size[1]

assert(size>1)

torch.manualSeed(rank)
sT=torch.rand(2,10)
rT=torch.zeros(2,10)
src=(rank-1)%size
dest=(rank+1)%size
tag=0
status=mpiT.Status(1)
--print(sT)
--print(rT)
if (rank == 0) then
   --the 20 below is the number of doubles
   mpiT.Send(sT:storage(),20,mpiT.DOUBLE,dest,tag,mpiT.COMM_WORLD)
   mpiT.Recv(rT:storage(),20,mpiT.DOUBLE,src,tag,mpiT.COMM_WORLD,status)
else
   mpiT.Recv(rT:storage(),20,mpiT.DOUBLE,src,tag,mpiT.COMM_WORLD,status)
   mpiT.Send(sT:storage(),20,mpiT.DOUBLE,dest,tag,mpiT.COMM_WORLD)
end
--print(sT)
--print(rT)

s2T=torch.rand(2,10)
reducedT=torch.DoubleTensor(2,10)
if rank>0 then
   -- print(s2T)
   mpiT.Reduce(s2T:storage(),reducedT:storage(),20,mpiT.DOUBLE,mpiT.SUM,0,mpiT.COMM_WORLD)
else
   mpiT.Reduce(s2T:storage(),reducedT:storage(),20,mpiT.DOUBLE,mpiT.SUM,0,mpiT.COMM_WORLD)
   print(reducedT)
end

print('rank ' .. rank .. ' done with Reduce')

mpiT.Bcast(reducedT:storage(), 20, mpiT.DOUBLE, 0, mpiT.COMM_WORLD)
print(reducedT)

mpiT.Finalize()
