counter = 0
wrk.method = "GET"

request = function()
    local path = "/v0/entity?id=" .. counter
    counter = counter + 1
    return wrk.format(nil, path)
end
