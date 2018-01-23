counter = 0
repeatLimit = 1000
wrk.method = "GET"

request = function()
    local path = "/v0/entity?id=" .. counter
    counter = (counter + 1) % repeatLimit
    return wrk.format(nil, path)
end
