class TrainEvent
	attr_accessor :time, :num_trains_change
	def initialize(time, num_trains_change) 
		@time, @num_trains_change = time, num_trains_change
	end
	def <=>(other)
		@time == other.time ? other.num_trains_change <=> @num_trains_change : @time <=> other.time 
	end	
end

input = File.new(ARGV[0])

def add_time(time, min_change)
	hour, min = time / 100, time % 100 + min_change
	if min >= 60
		hour += 1
		min -= 60
	end
	return hour * 100 + min
end

def findRequiredTrains(station_events)
	station_events.sort!
  
  trains_min, trains = 0,0
	
	station_events.each do |evt| 
		trains += evt.num_trains_change
		trains_min = [trains, trains_min].min		
	end
	
	return trains_min.abs
end

for testCase in 1..input.readline.to_i
	turnAroundTime = input.readline.to_i
	na, nb = input.readline.split.map { |s| s.to_i }
	station_a_events, station_b_events = [], []
  for i in 0...na + nb
  	startTime, doneTime = input.readline.split.map { |s| (s[0, 2] + s[3, 2]).to_i }
  	(i < na ? station_a_events : station_b_events).push(TrainEvent.new(startTime, -1))
		(i < na ? station_b_events : station_a_events).push(TrainEvent.new(add_time(doneTime, turnAroundTime), 1))
  end
	
	puts "Case ##{testCase}: #{findRequiredTrains(station_a_events)} #{findRequiredTrains(station_b_events)}"
end

